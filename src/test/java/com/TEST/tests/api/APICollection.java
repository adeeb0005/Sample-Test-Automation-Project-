package com.TEST.tests.api;

import com.TEST.utils.Config;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import io.restassured.path.json.JsonPath;

public class APICollection {

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
    public void frigaToken() {
        String frigaToken = apiClient.frigaToken();
        System.out.println(frigaToken);
    }

    @Test
    public void DataLoggerAPI001_PushLocationData() {
        String deviceId = "AutoDLJ001";
        String uid = "807938083";
        long unixTime = Instant.now().getEpochSecond();
        Integer vol = 50;
        Integer signal = 50;
        String longitude = "90.4029252";
        String latitude = "23.8434344";
        String desc = "Location Based Data";
        int src = 3;

        Response response = apiClient.pushLocationData(deviceId, uid, unixTime, vol, signal, longitude, latitude, desc, src);
        response.then().statusCode(200);
    }

    @Test
    public void DataLoggerAPI002_PushTelemetryData() {
        String deviceId = "AutoAPIDevice1738149810";
        String uid = "807938083";
        long unixTime = Instant.now().getEpochSecond();
        long lbstime = 1733488452;
        int t0 = 400;
        int t1 = 450;
        int h0 = 95;
        int h1 = 100;

        Response response = apiClient.pushTelemetryData(deviceId, uid, unixTime, lbstime, t0, t1, h0, h1);
        response.then().statusCode(200);
    }

    // Todo: data not getting pushed. need to fix
    @Test
    public void DataLoggerAPI003_PushTemperatureAndHumidityAndLocationData_TC0000() {
        String deviceId = "AutoAPIDevice1738149810";
        String uid = "807938083";
        long unixTime = Instant.now().getEpochSecond();
        int t0 = 100;
        int t1 = 150;
        int h0 = 25;
        int h1 = 30;
        Integer vol = 50;
        Integer signal = 50;
        String longitude = "90.4029252";
        String latitude = "23.8434344";
        String desc = "Location Based Data";
        int src = 3;

        Response response = apiClient.pushTemperatureHumidityLocationData(deviceId, uid, unixTime, t0, t1, h0, h1, vol, signal, longitude, latitude, desc, src);
        response.then().statusCode(200);
    }

    // -------------------------------------- AssetTypes --------------------------------------------------
    @Test
    public void AssetTypes001_GETAssetTypesResponse_TC0000() {
        Response response = apiClient.GETAssetType();
        response.then().statusCode(200);
    }

    // ------------------------------------ Carrier ------------------------------------------------------
    @Test
    public void Carriers001_GETCarriers_TC0000() {
        Response response = apiClient.GETCarriers();
        response.then().statusCode(200);
    }

    // --------------------------------------- Dashboard -----------------------------------------------------
    @Test
    public void Dashboard001_GetDashboardSummary_TC0000(){
        Response response = apiClient.GETDashboardSummary();
        response.then().statusCode(200);
    }

    @Test
    public void Dashboard002_GetDashboardOrganization_TC0000(){
        Response response = apiClient.GETDashboardOrganizations("1", "20");
        response.then().statusCode(200);
    }

    @Test
    public void Dashboard003_GetDashboardAssets_TC0000(){
        Response response = apiClient.GETDashboardAssets();
        response.then().statusCode(200);
    }

    @Test
    public void Dashboard004_GetDashboardShipments_TC0000(){
        Response response = apiClient.GETDashboardShipments();
        response.then().statusCode(200);
    }

    @Test
    public void Dashboard005_GetDashboardRecentShipments_TC0000(){
        String pageNumber = "1";
        String pageSize = "20";

        Response response = apiClient.GETDashboardRecentShipments(pageNumber, pageSize);
        response.then().statusCode(200);
    }

    // ------------------------------------------ Device Manufacturers ---------------------------------------
    @Test
    public void DeviceManufacturers001_GetDeviceManufacturers_TC0000(){
        Response response = apiClient.GETDeviceManufacturers();
        response.then().statusCode(200);
    }

    // ------------------------------------- DeviceTypes -------------------------------------------------
    @Test
    public void DeviceTypes001_GetDeviceTypes_TC0000(){
        Response response = apiClient.GETDeviceTypes();
        response.then().statusCode(200);
    }

    // --------------------------------------- Organization -------------------------------------------------
    @Test
    public void Organization001_GetOrganizations_TC0000(){
        Response response = apiClient.GETOrganizations();
        response.then().statusCode(200);
    }

    // --------------------------------------- SensorTypes ---------------------------------------------------
    @Test
    public void SensorTypes001_GetSensorTypes_TC0000(){
        Response response = apiClient.GETSensorTypes();
        response.then().statusCode(200);
    }

    // ---------------------------------- Rules ----------------------------------------
    @Test
    public void RulesAPI001_GetRuleSets_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETRuleSets(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void RulesAPI002_PostRuleSet_TC0000() {
        // Arrange
        long time = Instant.now().getEpochSecond();
        String ruleSetName = "AutoAPIRule" + time;
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        List<Map<String, Object>> sensorRuleItems = new ArrayList<>();

        // Add sensor rule items
        sensorRuleItems.add(Map.of(
                "sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59",
                "sensorTypeName", "probeTemp",
                "isEnabled", true,
                "minThreshold", "5",
                "maxThreshold", "10",
                "exceptionDuration", "1"
        ));
        sensorRuleItems.add(Map.of(
                "sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5",
                "sensorTypeName", "deviceTemp",
                "isEnabled", true,
                "minThreshold", "5",
                "maxThreshold", "10",
                "exceptionDuration", "2"
        ));

        String emails = "test@example.com";
        String visibilityLevel = "OrganizationOnly";

        Response response = apiClient.POSTRuleSet(ruleSetName, organizationId, sensorRuleItems, emails, visibilityLevel);
        response.then().statusCode(201);
    }

    // Todo
    @Test
    public void RulesAPI003_PutRuleSetsByRulesId_TC0000(){

    }

    @Test
    public void RulesAPI004_DeleteRuleSetsByRulesId_TC0000() {
        String rulesId = "5a7c7875-5a2d-4d2b-890e-1c7e602e9383";
        Response response = apiClient.DELETERuleSetsById(rulesId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void RulesAPI005_DeleteRuleSetsByBulk_TC0000() {

    }

    // Todo
    @Test
    public void RulesAPI006_GetRuleSetsExport_TC0000() {

    }

    @Test
    public void RulesAPI007_GetRulSetsSummaryByRulesId() {
        String rulesId = "cee5e53f-26f4-44d1-a946-64597cb206e0";
        Response response = apiClient.GETRuleSetsSummaryById(rulesId);
        response.then().statusCode(200);
    }

    // -------------------------------- Location ------------------------------------
    @Test
    public void LocationAPI001_GetLocations_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETLocations(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void LocationAPI002_PostLocations_TC0000() {
        long time = Instant.now().getEpochSecond();
        String locationName =  "AutoAPILocation" + time;
        String address = "M52M+866, N 106, Rangamati 4500, Bangladesh";
        String locationType = "Warehouse";
        String airportCode = "";
        double latitude = 22.65391800;
        double longitude = 92.18110800;
        double geofenceRadius = 500.0;
        String visibilityLevel = "OrganizationOnly";
        String organizationId = Config.APIOrgId;

        Response response = apiClient.POSTCreateLocation(locationName, address, locationType, airportCode, latitude, longitude, geofenceRadius, visibilityLevel, organizationId);
        response.then().statusCode(201);
    }

    @Test
    public void LocationAPI003_GetLocationByLocationId_TC0000() {
        String locationId = "1db99053-0a69-46d8-beb8-8145e2d54fbf";
        Response response = apiClient.GETLocationById(locationId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void LocationAPI004_PutLocationByLocationId_TC0000() {

    }

    @Test
    public void LocationAPI005_DeleteLocationByLocationId_TC0000() {
        String locationId = "b62f3263-5055-4a9f-ac51-6449ff1b5bf4";
        Response response = apiClient.DELETELocationById(locationId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void LocationAPI005_GetLocationExport_TC0000() {

    }

    // Todo
    @Test
    public void LocationAPI006_PostLocationBulkImport_TC0000() {

    }

    // Todo
    @Test
    public void LocationAPI007_DeleteLocationBulkDelete_TC0000() {

    }

    @Test
    public void LocationAPI008_GetLocationOriginDestination_TC0000() {
        String locationquerykey = "Origin"; // "Destination" or "Origin"
        String pageNumber = "4";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";
        Response response = apiClient.GETOriginsDestinationsOfLocations(locationquerykey, pageNumber, pageSize, sortColumns, sortDirections);
        response.then().statusCode(200);
    }

    // -------------------------------- routes -----------------------------------------
    @Test
    public void RoutesAPI001_PostRoutes_TC0000() {
        long time = Instant.now().getEpochSecond();
        String routeName = "AutoAPIRoute" + time;
        List<List<String>> locations = new ArrayList<>(List.of(
                //      List.of(locationId,                           , locationType);
                List.of("CCB314E8-1CDE-44B6-B034-014C68806754", "Airport"),
                List.of("7F0B87B7-E7D5-44DD-8672-027130C7CAF2", "Airport"),
                List.of("3D08295F-55D4-44A9-AF4B-04E811C4E571", "Warehouse")
        ));
        String visibilityLevel = "OrganizationOnly";
        String organizationId = Config.APIOrgId;
        List<Object> routesList = new ArrayList<>();
        for(int i = 0; i < locations.size() - 1; i++){
            String origin = locations.get(i).get(0);
            String originType = locations.get(i).get(1);
            String destination = locations.get(i + 1).get(0);
            String destinationType = locations.get(i + 1).get(1);
            String travelMode = "Road";
            if(originType.equals(destinationType) && originType.equals("Airport"))travelMode = "Air";
            Map<String, Object> routeSegmentsDetails = new HashMap<>();
            routeSegmentsDetails.put("originLocationId", origin);
            routeSegmentsDetails.put("destinationLocationId", destination);
            routeSegmentsDetails.put("travelMode", travelMode);
            routeSegmentsDetails.put("order", i + 1);
            routesList.add(routeSegmentsDetails);
        }
        Response response = apiClient.POSTCreateRoute(routeName, routesList, visibilityLevel, organizationId);
        response.then().statusCode(201);
    }

    @Test
    public void RoutesAPI002_GetRoutes_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETRoutes(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void RoutesAPI003_GetRoutesByRouteId_TC0000() {
        String routeId = "5119a622-0c5e-430e-93c0-7ed997f90aee";
        Response response = apiClient.GETRoutesById(routeId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void RoutesAPI004_PutRoutesByRouteId_TC0000() {

    }

    @Test
    public void RoutesAPI005_DeleteRoutesByRouteId_TC0000() {
        String routeId = "5119a622-0c5e-430e-93c0-7ed997f90aee";
        Response response = apiClient.DELETERouteById(routeId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void RoutesAPI001_GetRoutesExport_TC0000() {

    }

    // Todo
    @Test
    public void RoutesAPI001_DeleteRoutesByBulk_TC0000() {

    }

    // ------------------------------ Device Profiles ---------------------------------
    @Test
    public void DeviceProfiles001_GetDeviceProfiles_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETDeviceProfiles(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void DeviceProfiles002_PostDeviceProfiles_TC0000() {
        long time = Instant.now().getEpochSecond();
        String deviceProfileName = "AutoAPIDeviceProfile" + time;
        deviceProfileName = "Auto Profile RLT T72 " + time;
        String deviceTypeId = "2B1598D8-668D-4BE9-9937-966E3004D78D";
        int minTemperatureThreshold = 0;
        int maxTemperatureThreshold = 2;
        int recordingInterval = 1;
        int reportingInterval = 1;
        String visibilityLevel = "OrganizationOnly";
        String organizationId = Config.APIOrgId;
        Response response = apiClient.POSTCreateDeviceProfile(deviceProfileName,deviceTypeId,minTemperatureThreshold,
                maxTemperatureThreshold, recordingInterval,reportingInterval,visibilityLevel,organizationId);
        response.then().statusCode(201);
    }

    // Todo
    @Test
    public void DeviceProfiles003_GetDeviceProfilesExport_TC0000() {

    }

    @Test
    public void DeviceProfiles004_GetDeviceProfilesByDeviceProfileId_TC0000() {
        String deviceProfilesId = "39024472-ece6-4e17-91c9-b7cfd4fc91c7";
        Response response = apiClient.GETDeviceProfileById(deviceProfilesId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void DeviceProfiles005_PutDeviceProfilesByDeviceProfileId_TC0000() {

    }

    @Test
    public void DeviceProfiles006_DeleteDeviceProfilesByDeviceProfileId_TC0000() {
        String deviceProfilesId = "39024472-ece6-4e17-91c9-b7cfd4fc91c7";
        Response response = apiClient.DELETEDeviceProfileById(deviceProfilesId);
        response.then().statusCode(200);
    }

    @Test
    public void DeviceProfiles007_GetAssignedDevicesByDeviceProfileId_TC0000() {
        String deviceProfilesId = "89fc6b29-dc18-48ef-a43b-46dd79eda642";
        Response response = apiClient.GETAssignedDevicesDeviceProfileById(deviceProfilesId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void DeviceProfiles008_GetExportAssignedDevicesByDeviceProfileId_TC0000() {

    }

    // ------------------------------ Devices ---------------------------------------
    @Test
    public void DevicesAPI001_GetDevices_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETDevices(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void DevicesAPI002_PostDevices_TC0000() {
        String DeviceName = "AutoDevice004";
        String deviceTypeID = "37FECF1B-8170-4A69-9F72-2C5284F0CA7A";
        String deviceProfileID = "96CF238F-2278-4625-B558-CC8E9A43983A";
        String OrganizationID = "49194360-27AB-4434-991F-88116DC775BB";
        Response response = apiClient.POSTCreateDevice(DeviceName, deviceTypeID, deviceProfileID, OrganizationID);
        response.then().statusCode(201);
    }

    // Todo
    @Test
    public void DevicesAPI003_PatchDevicesByDeviceId_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI004_DeleteDevicesByDeviceId_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI005_GetDevicesByDeviceId_TC0000() {
        String deviceId = "78f2885b-9d04-4084-8afe-b2f1b7abc29a";
        Response response = apiClient.GETDeviceById(deviceId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void DevicesAPI006_GetAssetBindingHistoryByDeviceId_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI007_GetDevicesExport_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI008_GetExportAssetBindingHistoryByDeviceId_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI009_DeleteDevicesByBulk_TC0000() {

    }

    // Todo
    @Test
    public void DevicesAPI010_PostDevicesBulkImport_TC0000() {

    }

    @Test
    public void DevicesAPI011_CreateAndGetSingleDeviceDetails() {
        String DeviceName = "Auto Device OnAsset S100 012";
        String deviceTypeID = "d954af51-11c6-429e-91c5-51588edb7982";
        String deviceProfileID = "bdf341ff-069d-4476-8fd0-cb948254baf2";
        String OrganizationID = "49194360-27ab-4434-991f-88116dc775bb";
        Response response = apiClient.POSTCreateDevice(DeviceName, deviceTypeID, deviceProfileID, OrganizationID);
        response.then().statusCode(201);

        JsonPath jsonPath = response.jsonPath();
        String deviceId = jsonPath.getString("deviceId");
        // String deviceId = "b52b3911-59b2-4b29-bac7-e78837be7f15";
        Response detailsResponse = apiClient.GETDeviceById(deviceId);
        detailsResponse.then().statusCode(200);
    }
    // ------------------------- Assets ---------------------------------------
    @Test
    public void AssetsAPI001_GetAssets_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETAssets(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test
    public void AssetsAPI002_PostAssets_TC0000() {
        long time = Instant.now().getEpochSecond();
        // Replace with actual values
        String assetId = ""; // Can be empty as per the curl
        String assetName = "AutoAPIAsset" + time;
        String assetTypeId = "07B138D3-F7A8-437C-8511-ADF933855C28";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        String visibilityLevel = "OrganizationOnly";

        Response response = apiClient.POSTCreateAsset(assetId, assetName, assetTypeId, organizationId, visibilityLevel);
        response.then().statusCode(201);
    }

    // Todo
    @Test
    public void AssetsAPI003_GetAssetsByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI004_PatchAssetsByAssetId_TC0000() {
        long time = Instant.now().getEpochSecond();
        String assetId = "724455f6-6c5a-4681-71687d9a02f9";
        Map<String, String> assetDetails = new HashMap<>();
        assetDetails.put("assetId", assetId);
        assetDetails.put("assetName", "auto "+"Silverskin" + " "+time);
        assetDetails.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
        apiClient.PATCHAssetByAssetId(assetId, assetDetails);
    }

    // Todo
    @Test
    public void AssetsAPI005_DeleteAssetsByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI006_PostAssetsExternal_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI007_GetUnboundDevicesByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI008_GetBoundDevicesByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI009_PostBindAssetsByAssetId_TC0000() {
        String assetId = "06959874-ADA3-449D-84B4-03E5E52F7B81";
        String deviceId = "BE1C14BA-9B85-4D94-8732-5B0B5782D35C";
        String mountSlotId = null;
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> deviceDetails = new HashMap<>();
        deviceDetails.put("deviceId",deviceId);
        deviceDetails.put("mountSlotId", mountSlotId);
        devices.add(deviceDetails);
        Response response = apiClient.POSTAssetBindByAssetId(assetId, devices);
        response.then().statusCode(201);
    }

    // Todo
    @Test
    public void AssetsAPI010_PostUnbindAssetsByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI011_PostBindUnbinAssetsByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI012_GetAssetsSensorTypesByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI013_PostAssetsOrganizationByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI014_GetAssetsDeviceBindingHistoryByAssetId_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI015_GetAssetsExport_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI016_GetAssetsOverview_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI017_PostAssetsBulkImport_TC0000() {

    }

    // Todo: bulk delete for load and others
    @Test
    public void AssetsAPI018_DeleteAssetsBulkDelete_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI019_PostAssetsBulkBinding_TC0000() {

    }

    // Todo
    @Test
    public void AssetsAPI020_PostAssetsBulkUnbinding_TC0000() {

    }

    @Test
    public void AssetsAPI021_RenameListOfAssets_TC0000(){
        List<List<String>> assets = Arrays.asList(
               Arrays.asList("Softbox® VP", "2fa1114c-8081-4736-82ef2180ef7c", "AutoAPIAset1740574047")
        );
        List<List<String>> updatedData = new ArrayList<>();
        for(int i = 0; i < assets.size(); i++){
            long time = Instant.now().getEpochSecond();
            String assetId = assets.get(i).get(1);
            String assetName = "auto "+assets.get(i).get(0) + " "+time;
            Map<String, String> assetDetails = new HashMap<>();
            assetDetails.put("assetId", assetId);
            assetDetails.put("assetName", assetName);
            assetDetails.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
            Response response = apiClient.PATCHAssetByAssetId(assetId, assetDetails);
            List<String> responseData = new ArrayList<>();
            responseData.add(assets.get(i).get(0));
            responseData.add(assets.get(i).get(1));
            responseData.add(assetName);
            updatedData.add(responseData);
        }
        System.out.println(updatedData);
    }
    // -------------------------------- shipments --------------------------------------
    @Test
    public void ShipmentsAPI001_GetShipmentsByShipmentId_TC0000() {
        String ShipmentId = "80837ebc-3899-4677-8c66-e5c02801feff";
        Response response = apiClient.GETShipmentByShipmentId(ShipmentId);

        // Validate the response
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void ShipmentsAPI002_PutShipmentsByShipmentId_TC0000() {

    }

    @Test
    public void ShipmentsAPI003_DeleteShipmentsByShipmentId_TC0000() {
        String shipmentId = "78955866-8e7c-4379-9db5-0ae0c71375f1";
        Response response = apiClient.DELETEShipmentById(shipmentId);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void ShipmentsAPI004_GetAssetDetailsByShipmentIdAndAssetId_TC0000() {

    }

    @Test
    public void ShipmentsAPI005_PostShipments_TC0000() {
        long time = Instant.now().getEpochSecond();
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

        String organizationId = "49194360-27AB-4434-991F-88116DC775BB";

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
        asset.put("assetId", "A79AB687-A2BC-40F1-A2D1-B632A7C7BE5E");
        asset.put("assetId", "267FB72C-8D5A-4D12-BCB5-8208FFEAAFA8");

        assets.add(asset);
        Response response = apiClient.POSTShipmentsCreate(shipmentName, shipperName, orderNumber, CarrierId, travelMode,
                customerName, recipientName, shipmentType, shipmentDirection, airwayBillNumber,
                emails, plannedDepartureDate, plannedArrivalDate, organizationId, ruleIds, deviceProfileId,
                routeOrLocationChoice, isDynamicOriginLocation, dynamicOriginLocation, isDynamicDestinationLocation,
                dynamicDestinationLocation,routeId, originLocationId, destinationLocationId, assets);
        response.then().statusCode(201);
    }

    @Test
    public void ShipmentsAPI006_GetShipments_TC0000() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETShipments(pageNumber, pageSize, sortColumns, sortDirections);

        // Validate the response
        response.then().statusCode(200);
    }

    @Test
    public void ShipmentsAPI007_GetShipmentsSummaryByShipmentId_TC0000() {
        String shipmentID = "80837ebc-3899-4677-8c66-e5c02801feff";

        Response response = apiClient.GETShipmentSummaryByShipmentId(shipmentID);
        response.then().statusCode(200);
    }

    @Test
    public void ShipmentsAPI008_PostShipmentsCompleteByShipmentId_TC0000() {
        String shipmentID = "3598a8d2-1457-4128-a459-a7dcea60fdfe";

        Response response = apiClient.POSTShipmentCompleteByShipmentId(shipmentID);
        response.then().statusCode(200);
    }

    // Todo
    @Test
    public void ShipmentsAPI009_DeleteShipmentsBulkDelete_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI010_GetShipmentsExport_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI011_PostShipmentsReportByShipmentId_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI012_GetShipmentsReportByShipmentId_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI013_PostShipmentsParcelBulkImport_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI014_PostShipmentsCellAndGeneBulkImport_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI015_PostShipmentsMigrate_TC0000() {

    }

    // Todo
    @Test
    public void ShipmentsAPI016_PostShipmentsCompleteMigration_TC0000() {

    }

    // =================================================================================================

    public String findValueThroughKeyResponse(String Key, Response response){
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        return jsonResponse.getString(Key);
    }

    @Test
    public void CustomRuleSetsAPI001_CreateMultipleRuleSets() {
        int numberOfItems = 24;
        List<String> listOfRuleId = new ArrayList<>();
        for(int i = 0; i < numberOfItems; i++){
            long time = Instant.now().getEpochSecond();
            String ruleSetName = "LoadRuleSets" + time;
            String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
            List<Map<String, Object>> sensorRuleItems = new ArrayList<>();


            // Add sensor rule items
            sensorRuleItems.add(Map.of(
                    "sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59",
                    "sensorTypeName", "probeTemp",
                    "isEnabled", true,
                    "minThreshold", "5",
                    "maxThreshold", "10",
                    "exceptionDuration", "1"
            ));
            sensorRuleItems.add(Map.of(
                    "sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5",
                    "sensorTypeName", "deviceTemp",
                    "isEnabled", true,
                    "minThreshold", "5",
                    "maxThreshold", "10",
                    "exceptionDuration", "2"
            ));


            String emails = "test@example.com";
            String visibilityLevel = "OrganizationOnly";


            Response createRuleSetsResponse = apiClient.POSTRuleSet(ruleSetName, organizationId, sensorRuleItems, emails, visibilityLevel);
            createRuleSetsResponse.then().statusCode(201);
            String ruleId = createRuleSetsResponse.jsonPath().getString("ruleId");
//            String ruleId = findValueThroughKeyResponse("ruleId", createRuleSetsResponse);
            listOfRuleId.add(ruleId);
        }
        System.out.println(listOfRuleId);
        System.out.println(listOfRuleId.size());
    }

    @Test
    public void CustomRuleSetsAPI002_SearchRuleSets() {
        String pageNumber = "1";
        String pageSize = "5";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("ruleSetName", "~LoadRuleSets");

        Response response = apiClient.GETRuleSets(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomRuleSetsAPI003_SearchAndDeleteRuleSetsByPartialName() {
        String pageNumber = "1";
        String pageSize = "10";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("ruleSetName", "~LoadRuleSets");

        Response response = apiClient.GETRuleSets(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();
        List<String> ruleSetIds = jsonPath.getList("items.ruleSetId");

        System.out.println(ruleSetIds);
        System.out.println(ruleSetIds.size());
        Response deleteResponse = apiClient.DELETERulesSetsBulkDelete(ruleSetIds);
        deleteResponse.then().statusCode(200);
    }

    @Test
    public void CustomAreasAPI001_CreateMultipleAreas() {
        int numberOfItems = 24;
        List<String> areaIds = new ArrayList<>();

        for(int i = 0; i < numberOfItems; i++){
            long time = Instant.now().getEpochSecond();
            String locationName =  "AutoAPILocation" + time;
            String address = "M52M+866, N 106, Rangamati 4500, Bangladesh";
            String locationType = "Warehouse";
            String airportCode = "";
            double latitude = 22.65391800;
            double longitude = 92.18110800;
            double geofenceRadius = 500.0;
            String visibilityLevel = "OrganizationOnly";
            String organizationId = Config.APIOrgId;

            Response response = apiClient.POSTCreateLocation(locationName, address, locationType, airportCode, latitude, longitude, geofenceRadius, visibilityLevel, organizationId);
            response.then().statusCode(201);
            String areaId = response.jsonPath().getString("locationName");
            areaIds.add(areaId);
        }
        System.out.println(areaIds);
        System.out.println(areaIds.size());
    }

    @Test
    public void CustomAreasAPI002_SearchAreas() {
        String pageNumber = "1";
        String pageSize = "5";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("locationName", "~LoadLocation");

        Response response = apiClient.GETLocations(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomAreasAPI003_SearchAndDeleteAreasByPartialName() {
        String pageNumber = "1";
        String pageSize = "500";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("locationName", "~LoadLocation");

        Response response = apiClient.GETLocations(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();
        List<String> areaIds = jsonPath.getList("items.locationId");

        System.out.println(areaIds);
        System.out.println(areaIds.size());
        Response deleteResponse = apiClient.DeleteLocationBulkDelete(areaIds);
        deleteResponse.then().statusCode(200);
    }

    @Test
    public void CustomRoutesAPI001_CreateMultipleRoutes() {
        int numberOfItems = 2;
        List<String> routeIds = new ArrayList<>();

        for(int i = 0; i < numberOfItems; i++){
            long time = Instant.now().getEpochSecond();
            String routeName = "AutoAPIRoute" + time;
            List<List<String>> locations = new ArrayList<>(List.of(
                    //      List.of(locationId,                           , locationType);
                    List.of("CCB314E8-1CDE-44B6-B034-014C68806754", "Airport"),
                    List.of("7F0B87B7-E7D5-44DD-8672-027130C7CAF2", "Airport"),
                    List.of("3D08295F-55D4-44A9-AF4B-04E811C4E571", "Warehouse")
            ));
            String visibilityLevel = "OrganizationOnly";
            String organizationId = Config.APIOrgId;
            List<Object> routesList = new ArrayList<>();
            for(int j = 0; j < locations.size() - 1; j++){
                String origin = locations.get(j).get(0);
                String originType = locations.get(j).get(1);
                String destination = locations.get(j + 1).get(0);
                String destinationType = locations.get(j + 1).get(1);
                String travelMode = "Road";
                if(originType.equals(destinationType) && originType.equals("Airport"))travelMode = "Air";
                Map<String, Object> routeSegmentsDetails = new HashMap<>();
                routeSegmentsDetails.put("originLocationId", origin);
                routeSegmentsDetails.put("destinationLocationId", destination);
                routeSegmentsDetails.put("travelMode", travelMode);
                routeSegmentsDetails.put("order", j + 1);
                routesList.add(routeSegmentsDetails);
            }
            Response response = apiClient.POSTCreateRoute(routeName, routesList, visibilityLevel, organizationId);
            response.then().statusCode(201);
            String areaId = response.jsonPath().getString("routeId");
            routeIds.add(areaId);
        }
        System.out.println(routeIds);
        System.out.println(routeIds.size());
    }

    @Test
    public void CustomRoutesAPI002_SearchRoutes() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("routeName", "~LoadRoutes");

        Response response = apiClient.GETRoutes(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomRoutesAPI003_SearchAndDeleteRoutesByPartialName() {
        String pageNumber = "1";
        String pageSize = "500";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("routeName", "~LoadRoutes");

        Response response = apiClient.GETRoutes(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();

        // Extract list of maps representing each route
        List<Map<String, Object>> routes = jsonPath.getList("items");

        // Filter and collect routeIds where onShipment is false
        List<String> routeIds = routes.stream()
                .filter(route -> !(Boolean.TRUE.equals(route.get("onShipment")))) // Check if onShipment is not true
                .map(route -> (String) route.get("routeId")) // Extract routeId
                .collect(Collectors.toList());

        System.out.println(routeIds);
        System.out.println(routeIds.size());
        if (!routeIds.isEmpty()) { // Ensure there are routes to delete
            Response deleteResponse = apiClient.DELETERoutesBulkDelete(routeIds);
            deleteResponse.then().statusCode(200);
        } else {
            System.out.println("No routes to delete.");
        }
    }

    // Todo
    @Test
    public void CustomDeviceProfilesAPI001_CreateMultipleDeviceProfiles() {

    }

    @Test
    public void CustomDeviceProfilesAPI002_SearchDeviceProfiles() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("deviceProfileName", "~LoadDeviceProfile");

        Response response = apiClient.GETDeviceProfiles(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomDeviceProfilesAPI003_SearchAndDeleteDeviceProfilesByPartialName() {
        String pageNumber = "1";
        String pageSize = "999";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map<String, String> filter = new HashMap<>();
        filter.put("deviceProfileName", "~LoadDeviceProfile");

        Response response = apiClient.GETDeviceProfiles(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();

        // Extract list of maps representing each device profile
        List<Map<String, Object>> deviceProfiles = jsonPath.getList("items");

        // Filter device profiles where onShipment is false and isUsedInDevice is false
        List<String> deviceProfileIds = deviceProfiles.stream()
                .filter(profile -> !(Boolean.TRUE.equals(profile.get("onShipment"))) &&
                        !(Boolean.TRUE.equals(profile.get("isUsedInDevice"))))
                .map(profile -> (String) profile.get("deviceProfileId"))
                .collect(Collectors.toList());

        System.out.println(deviceProfileIds);
        System.out.println("Total profiles to delete: " + deviceProfileIds.size());

        if (!deviceProfileIds.isEmpty()) {
            for (String deviceProfileId : deviceProfileIds) {
                Response deleteResponse = apiClient.DELETEDeviceProfileById(deviceProfileId);

                if (deleteResponse.getStatusCode() == 200 || deleteResponse.getStatusCode() == 204) {
                    System.out.println("Deleted device profile: " + deviceProfileId);
                } else {
                    System.out.println("Failed to delete device profile: " + deviceProfileId +
                            " | Status Code: " + deleteResponse.getStatusCode());
                }
            }
        } else {
            System.out.println("No device profiles to delete.");
        }
    }

    // Todo
    @Test
    public void CustomDevicesAPI001_CreateMultipleDevices() {

    }

    @Test
    public void CustomDevicesAPI002_SearchDevices() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("deviceName", "~LoadDevice");

        Response response = apiClient.GETDevices(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomDevicesAPI003_SearchAndDeleteDevicesByPartialName() {
        String pageNumber = "1";
        String pageSize = "500";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("deviceName", "~LoadDevice");

        Response response = apiClient.GETDevices(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();

        List<Map<String, Object>> devices = jsonPath.getList("items");

        // Filter and collect deviceIds where status is not "Active" (if applicable)
        List<Map<String, String>> deviceDetails = devices.stream()
                .filter(device -> device.get("assetId") == null && device.get("asset") == null && !((Boolean) device.get("onShipment"))) // Adjust condition based on response structure
                .map(device -> {
                    Map<String, String> details = new HashMap<>();
                    details.put("deviceId", (String) device.get("deviceId"));
                    details.put("deviceName", (String) device.get("deviceName"));
                    return details;
                })
                .collect(Collectors.toList());

        System.out.println("Devices to delete:");
        for (Map<String, String> device : deviceDetails) {
            System.out.println("Device ID: " + device.get("deviceId") + " | Device Name: " + device.get("deviceName"));
        }

        System.out.println("Total devices found: " + deviceDetails.size());

        // Check if there are devices to delete
        if (!deviceDetails.isEmpty()) {
            // Send DELETE request to delete devices in bulk
            List<String> deviceIds = deviceDetails.stream()
                    .map(device -> device.get("deviceId"))
                    .collect(Collectors.toList());

            Response deleteResponse = apiClient.DELETEDevicesBulkDelete(deviceIds);
            deleteResponse.then().statusCode(200);
            System.out.println("Devices deleted successfully.");
        } else {
            System.out.println("No devices to delete.");
        }
    }

    @Test
    public void CustomAssetsAPI001_SearchAssets() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("assetName", "~LoadAsset");
        // filter.put("assetType.assetTypeName", "doglapan RKN");
        // filter.put("devices.deviceName", "Tradoglapan Pro abcd"); // bound device name
        // filter.put("assetStatus", "Monitored"); // or "UnMonitored" or "Monitored.UnMonitored"
        // filter.put("currentLocation", "Out of Coverage"); // currentLocation is also called geolocation and value can be "~Lat:44.5"
        // filter.put("shipment.shipmentName", null); // deprecated
        // filter.put("organization", null);
        // filter.put("createdAt", "2024-1-01|2024-10-10"); // deprecated
        // filter.put("createdBy", "");
        // filter.put("lastModifiedBy", "");

        Response response = apiClient.GETAssets(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);
    }

    @Test
    public void CustomAssetsAPI002_SearchAndDeleteAssetsByPartialName() {
        String pageNumber = "1";
        String pageSize = "500";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Map <String, String> filter = new HashMap<>();
        filter.put("assetName", "~LoadAsset");

        Response response = apiClient.GETAssets(pageNumber, pageSize, sortColumns, sortDirections, filter);
        response.then().statusCode(200);

        JsonPath jsonPath = response.jsonPath();
//        List<String> assetIds = jsonPath.getList("items.assetId");
        List<String> assetIds = jsonPath.getList("items.findAll { it.shipmentId == null && it.shipment == null }.assetId");

        System.out.println(assetIds);
        System.out.println(assetIds.size());
        Response deleteResponse = apiClient.DELETEAssetsBulkDelete(assetIds);
        deleteResponse.then().statusCode(200);
    }


}
