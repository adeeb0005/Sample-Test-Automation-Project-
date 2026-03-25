package com.TEST.tests.api;
import com.TEST.utils.Config;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileReader;

public class E2E {
    private ApiClient apiClient;

    @BeforeClass
    public void setup() {
        // Initialize the ApiClient with the base URL from the properties file
        String baseUrl = Config.APIBaseUrl;
        String accessToken = Config.APIAccessToken;
        String organizationId = Config.APIOrgId;
        apiClient = new ApiClient(baseUrl, accessToken, organizationId);
    }

    @Test
    public void e2e()
    {
        // Creating Asset --------------------------------------------------->
        long time = Instant.now().getEpochSecond();
        String assetId = ""; // Can be empty as per the curl
        String assetName = "AutoAPIAsset" + time;
        String assetTypeId = "07B138D3-F7A8-437C-8511-ADF933855C28";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        String visibilityLevel = "OrganizationOnly";

        Response response = apiClient.POSTCreateAsset(assetId, assetName, assetTypeId, organizationId, visibilityLevel);
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString); // JSON Object e convert
        String newAssetId = extractValueFromResponse(response, "assetId");

        // Create Device --------------------------------------------------->
        String DeviceName = "AutoDevice" + time;
        String deviceTypeID = "B6C1FCC6-EEC5-47B7-88E0-626DB35EDE87";
        String deviceProfileID = "96CF238F-2278-4625-B558-CC8E9A43983A";
        String OrganizationID = "49194360-27AB-4434-991F-88116DC775BB";
        Response deviceResponse = apiClient.POSTCreateDevice(DeviceName, deviceTypeID, deviceProfileID, OrganizationID);
        deviceResponse.then().statusCode(201);
        String newDeviceId = extractValueFromResponse(deviceResponse, "deviceId");

        // Bound Asset & Devices --------------------------------------------------->
        String mountSlotId = null;
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> deviceDetails = new HashMap<>();
        deviceDetails.put("deviceId",newDeviceId);
        deviceDetails.put("mountSlotId", mountSlotId);
        devices.add(deviceDetails);
        Response boundResponse = apiClient.POSTAssetBindByAssetId(newAssetId, devices);
        boundResponse.then().statusCode(200);
        System.out.println(boundResponse.prettyPrint());

        // Shipment Create --------------------------------------------------->
        String shipmentName = "AutoAPIShipment" + time;
        String shipperName = "AutoAPIShipper" + time;
        String orderNumber = "AutoOrderNumber" + time;
        String CarrierId = "1CD5C6B1-9224-45AB-BCC3-FF5EBCDC1AE0";
        String travelMode = "";
        String customerName = "AutoAPICustomer" + time;
        String recipientName = "AutoAPIRecipient" + time;

        String shipmentType = "Bulk Air Cargo";

        String shipmentDirection = "Outbound";
        String airwayBillNumber = "";
        String emails = "auto@doglapanglobal.com,adeebmuhaimin@gmail.com";

        String plannedArrivalDate = "2026-12-03T17:50:00.000Z";
        String plannedDepartureDate = "2026-12-02T17:50:00.000Z";


        List<String> ruleIds = new ArrayList<>();
        ruleIds.add("30E138F0-AD19-4AC4-A520-8358B9523704");
        ruleIds.add("21F0870D-ED69-4BA8-94D6-88DF9B3875BA");

        String deviceProfileId = "96CF238F-2278-4625-B558-CC8E9A43983A";

        String routeOrLocationChoice = "Route";

        boolean isDynamicOriginLocation = false;
        Map<String, Object>  dynamicOriginLocation = new HashMap<>();

        boolean isDynamicDestinationLocation = false;
        Map<String, Object>  dynamicDestinationLocation = new HashMap<>();

        String routeId = "86D4FE1F-989F-4548-A1DB-84A65D3DD456";
        String originLocationId = "CCB314E8-1CDE-44B6-B034-014C68806754";
        String destinationLocationId = "3D08295F-55D4-44A9-AF4B-04E811C4E571";

        List<Map<String, String>> assets = new ArrayList<>();
        Map<String, String> asset = new HashMap<>();
        asset.put("assetId", newAssetId);

        assets.add(asset);
        Response shipmentResponse = apiClient.POSTShipmentsCreate(shipmentName, shipperName, orderNumber, CarrierId, travelMode,
                customerName, recipientName, shipmentType, shipmentDirection, airwayBillNumber,
                emails, plannedDepartureDate, plannedArrivalDate, organizationId, ruleIds, deviceProfileId,
                routeOrLocationChoice, isDynamicOriginLocation, dynamicOriginLocation, isDynamicDestinationLocation,
                dynamicDestinationLocation,routeId, originLocationId, destinationLocationId, assets);
        shipmentResponse.then().statusCode(201);
        String newShipmentId = extractValueFromResponse(shipmentResponse, "shipmentId");
        // Save the IDs to a JSON file
        saveIdsToJsonFile(newDeviceId, newAssetId, newShipmentId, "src/test/resources/testdata/generated_ids.json");
    }
    private String extractValueFromResponse(Response response, String key) {
        // Convert response body to string
        String jsonString = response.body().asString();

        // Convert string to JSON object
        JSONObject jsonResponse = new JSONObject(jsonString);

        // Return the value associated with the specified key
        return jsonResponse.getString(key);
    }
    // Method to save IDs to a JSON file
    private void saveIdsToJsonFile(String deviceId, String assetId, String shipmentId, String filePath) {
        // Create a JSON object to store the IDs
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("assetId", assetId);
        jsonObject.put("shipmentId", shipmentId);

        // Create the file object for the target file path
        File file = new File(filePath);

        try {
            // If the file doesn't exist, create the file and any missing parent directories
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Create missing directories
                file.createNewFile(); // Create the file if it doesn't exist
            }

            // Write the JSON object to the file
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonObject.toString(4)); // Indentation level 4 for pretty printing
                System.out.println("Successfully saved the IDs to " + filePath);
            }

        } catch (IOException e) {
            System.err.println("An error occurred while saving to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @Test
    public void e2e_delete()
    {
        String filePath = "src/test/resources/testdata/generated_ids.json";

        // Create a File object for the given file path
        File file = new File(filePath);

        // Read the JSON object from the file
        try (FileReader fileReader = new FileReader(file)) {
            // Create a char array to hold the file contents
            char[] chars = new char[(int) file.length()];
            fileReader.read(chars);
            String jsonString = new String(chars);

            // Convert the string to a JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Extract the values of the IDs
            String deviceId = jsonObject.getString("deviceId");
            String assetId = jsonObject.getString("assetId");
            String shipmentId = jsonObject.getString("shipmentId");

            // Shipment
            Response shipmentResponse = apiClient.DELETEShipmentById(shipmentId);
            System.out.println(shipmentResponse.prettyPrint());
            shipmentResponse.then().statusCode(200);

            // Unbind Devices
            List<String> devices = new ArrayList<>();
            devices.add(deviceId);  // Add the extracted deviceId into the list.
            Response unbindResponse = apiClient.POSTUnBindDevice(assetId, devices);
            System.out.println(unbindResponse.prettyPrint());
            unbindResponse.then().statusCode(200);

            // Delete Device
            Response deviceResponse = apiClient.DELETEDeviceById(deviceId);
            System.out.println(deviceResponse.prettyPrint());
            deviceResponse.then().statusCode(200);

            // Asset Delete
            Response assetResponse = apiClient.DELETEAssetById(assetId);
            System.out.println(assetResponse.prettyPrint());
            assetResponse.then().statusCode(200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
