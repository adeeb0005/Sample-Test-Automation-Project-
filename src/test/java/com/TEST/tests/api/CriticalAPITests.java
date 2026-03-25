package com.TEST.tests.api;

import com.TEST.pages.DashboardPage;
import com.TEST.pages.LoginPage;
import com.TEST.tests.BaseTest;
import com.TEST.utils.Config;
import com.TEST.utils.DBUtility;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v127.network.Network;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.TEST.pages.BasePage.convertResultSetToList;

public class CriticalAPITests extends BaseTest{

    @Test(groups = {"API"})
    public void frigaToken() {
        String frigaToken = apiClient.frigaToken();
        System.out.println(frigaToken);
    }

    @Test(groups = {"API"})
    public void GetDashboardSummary(){
        Response response = apiClient.GETDashboardSummary();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDashboardOrganization(){
        Response response = apiClient.GETDashboardOrganizations("1", "20");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDashboardAssets(){
        Response response = apiClient.GETDashboardAssets();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDashboardShipments(){
        Response response = apiClient.GETDashboardShipments();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDashboardRecentShipments(){
        Response response = apiClient.GETDashboardRecentShipments("1", "20");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDeviceManufacturers(){
        Response response = apiClient.GETDeviceManufacturers();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDeviceTypes(){
        Response response = apiClient.GETDeviceTypes();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetOrganizations(){
        Response response = apiClient.GETOrganizations();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetSensorTypes(){
        Response response = apiClient.GETSensorTypes();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetRules() {
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETRuleSets(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    public Map<String, Object> rulesDataInfo() {
        long time = Instant.now().toEpochMilli();
        Map<String, Object> rulesData = new HashMap<>(Map.of(
                "ruleSetName", "AutoAPIRule" + time,
                "organizationId", "49194360-27ab-4434-991f-88116dc775bb",
                "emails", "test@example.com",
                "visibilityLevel", "OrganizationOnly",
                "sensorRuleItems", List.of(
                        Map.of("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59", "sensorTypeName", "probeTemp",
                                "isEnabled", true, "minThreshold", "5", "maxThreshold", "10", "exceptionDuration", "1"),
                        Map.of("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5", "sensorTypeName", "deviceTemp",
                                "isEnabled", true, "minThreshold", "5", "maxThreshold", "10", "exceptionDuration", "2")
                )
        ));
        return rulesData;
    }

    public Response createRuleSet(){
        Map<String, Object> rulesData = rulesDataInfo();
        Response response = apiClient.POSTRuleSet( (String) rulesData.get("ruleSetName"),  (String) rulesData.get("organizationId"),
                (List<Map<String, Object>>)rulesData.get("sensorRuleItems"), (String) rulesData.get("emails"), (String)rulesData.get("visibilityLevel"));
        return response;
    }

    public String findValueThroughKeyResponse(String Key, Response response){
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String Value = jsonResponse.getString(Key);
        return Value;
    }

    @Test(groups = {"API"})
    public void CreateRuleSet() {
        Response createRuleResponse = createRuleSet();
        createRuleResponse.then().statusCode(201);
        String rulesetId = findValueThroughKeyResponse("ruleId", createRuleResponse);
        apiClient.DELETERuleSetsById(rulesetId);
    }

    @Test(groups = {"API"})
    public void PutRuleSet() throws InterruptedException {
        Map<String, Object> rulesData = rulesDataInfo();
        Response response = apiClient.POSTRuleSet( (String) rulesData.get("ruleSetName"),  (String) rulesData.get("organizationId"),
                (List<Map<String, Object>>)rulesData.get("sensorRuleItems"), (String) rulesData.get("emails"), (String)rulesData.get("visibilityLevel"));
        String ruleId = findValueThroughKeyResponse("ruleId", response);
        response = apiClient.GETRuleSetsSummaryById(ruleId);
        String rulesetId = findValueThroughKeyResponse("ruleSetId", response);
        rulesData.replace("emails", "abcd@doglapangolbal.co");
        rulesData.put("ruleSetId", rulesetId);
        rulesData.remove("organizationId");
        rulesData.remove("visibilityLevel");
        Thread.sleep(5000);
        response = apiClient.PUTRuleSet(rulesetId, rulesData);
        response.then().statusCode(200);
        apiClient.DELETERuleSetsById(ruleId);
    }

    @Test(groups = {"API"})
    public void DeleteRuleSet(){
        Response createRuleResponse = createRuleSet();
        String rulesetId = findValueThroughKeyResponse("ruleId", createRuleResponse);
        Response deleteResponse = apiClient.DELETERuleSetsById(rulesetId);
        deleteResponse.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void DeleteRuleSetBulk(){
        int numberOfRuleSet = 5;
        List<String> listOfRulesId = new ArrayList<>();
        for(int i = 0; i < numberOfRuleSet; i++){
            Response createRuleResponse = createRuleSet();
            String rulesetId = findValueThroughKeyResponse("ruleId", createRuleResponse);
            listOfRulesId.add(rulesetId);
        }
        System.out.println(listOfRulesId);
        Response deleteResponse = apiClient.DELETERulesSetsBulkDelete(listOfRulesId);
        deleteResponse.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GETRuleSetsExport(){
        Response response = apiClient.GETRuleSetsExport();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetRuleSetIdSummary() {
        Response response = createRuleSet();
        String rulesetId =  findValueThroughKeyResponse("ruleId",  response);
        response = apiClient.GETRuleSetsSummaryById(rulesetId);
        response.then().statusCode(200);
        apiClient.DELETERuleSetsById(rulesetId);
    }

    @Test(groups = {"API"})
    public void GetLocations(){
        Response response = apiClient.GETLocations("1", "20", "UpdatedAt", "desc", null);
        response.then().statusCode(200);
    }

    public Map<String, Object> locationDataInfo() {
        return new HashMap<>(Map.of(
                "locationName", "AutoAPILocation" + Instant.now().toEpochMilli(),
                "address", "M52M+866, N 106, Rangamati 4500, Bangladesh",
                "locationType", "Warehouse",
                "airportCode", "",
                "latitude", 22.65391800,
                "longitude", 92.18110800,
                "geofenceRadius", 500.0,
                "visibilityLevel", "OrganizationOnly",
                "organizationId", Config.APIOrgId
        ));
    }

    public Response createLocationResponse(){
        Map<String, Object> locationData = locationDataInfo();
        Response response = apiClient.POSTCreateLocation((String) locationData.get("locationName"),
                (String)locationData.get("address"), (String)locationData.get("locationType"),
                (String)locationData.get("airportCode"), (double)locationData.get("latitude"),
                (double)locationData.get("longitude"), (double)locationData.get("geofenceRadius"),
                (String)locationData.get("visibilityLevel"), (String)locationData.get("organizationId"));
        return response;
    }

    @Test(groups = {"API"})
    public void CreateLocation(){
        Response response = createLocationResponse();
        response.then().statusCode(201);
        String locationId = findValueThroughKeyResponse("locationId", response);
        apiClient.DELETELocationById(locationId);
    }

    @Test(groups = {"API"})
    public void GetLocationById(){
        Response response = createLocationResponse();
        String locationId = findValueThroughKeyResponse("locationId", response);
        response = apiClient.GETLocationById(locationId);
        response.then().statusCode(200);
        apiClient.DELETELocationById(locationId);
    }

    @Test(groups = {"API"})
    public void PutLocationById(){
        Map<String, Object> locationData = locationDataInfo();
        Response response = apiClient.POSTCreateLocation((String) locationData.get("locationName"),
                (String)locationData.get("address"), (String)locationData.get("locationType"),
                (String)locationData.get("airportCode"), (double)locationData.get("latitude"),
                (double)locationData.get("longitude"), (double)locationData.get("geofenceRadius"),
                (String)locationData.get("visibilityLevel"), (String)locationData.get("organizationId"));
        locationData.remove("organizationId");
        String locationId = findValueThroughKeyResponse("locationId", response);
        locationData.put("locationId", locationId);
        locationData.replace("geofenceRadius", 1000.0);
        response = apiClient.PUTLocation(locationId, locationData);
        response.then().statusCode(200);
        apiClient.DELETELocationById(locationId);
    }

    @Test(groups = {"API"})
    public void DeleteLocationById(){
        Response response = createLocationResponse();
        String locationId = findValueThroughKeyResponse("locationId", response);
        response = apiClient.DELETELocationById(locationId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetLocationExport(){
        Response response = apiClient.GETLocationExport();
        response.then().statusCode(200);
    }

    // Todo: post location bulk import

    @Test(groups = {"API"})
    public void DeleteLocationBulk(){
        int numberOfLocation = 5;
        List<String> listOfLocationId = new ArrayList<>();
        for(int i = 0; i < numberOfLocation; i++){
            Response createlocationResponse = createLocationResponse();
            String locationId = findValueThroughKeyResponse("locationId", createlocationResponse);
            listOfLocationId.add(locationId);
        }
        Response deleteResponse = apiClient.DeleteLocationBulkDelete(listOfLocationId);
        deleteResponse.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetLocationOfOriginsDestinations(){
        String locationquerykey = "Origin"; // "Destination" or "Origin"
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";
        Response response = apiClient.GETOriginsDestinationsOfLocations(locationquerykey, pageNumber, pageSize, sortColumns, sortDirections);
        response.then().statusCode(200);
    }

    public Map<String, Object> routeDataInfo() {
        List<List<String>> locations = List.of(
                List.of("CCB314E8-1CDE-44B6-B034-014C68806754", "Airport"),
                List.of("7F0B87B7-E7D5-44DD-8672-027130C7CAF2", "Airport"),
                List.of("3D08295F-55D4-44A9-AF4B-04E811C4E571", "Warehouse")
        );
        List<Map<String, Object>> routesList = new ArrayList<>();
        for (int i = 0; i < locations.size() - 1; i++) {
            Map<String, Object> route = new HashMap<>();
            route.put("originLocationId", locations.get(i).get(0));
            route.put("destinationLocationId", locations.get(i + 1).get(0));
            route.put("travelMode", locations.get(i).get(1).equals("Airport") && locations.get(i + 1).get(1).equals("Airport") ? "Air" : "Road");
            route.put("order", i + 1);
            routesList.add(route);
        }
        Map<String, Object> routeData = new HashMap<>();
        routeData.put("routeName", "AutoAPIRoute" + Instant.now().toEpochMilli());
        routeData.put("routesList", routesList);
        routeData.put("visibilityLevel", "OrganizationOnly");
        routeData.put("organizationId", Config.APIOrgId);

        return routeData;
    }

    public Response createRouteResponse(){
        Map<String, Object> routeData = routeDataInfo();
        Response response = apiClient.POSTCreateRoute((String) routeData.get("routeName"),(List<Object>)routeData.get("routesList"),
                (String) routeData.get("visibilityLevel"), (String)routeData.get("organizationId"));
        return response;
    }
    @Test(groups = {"API"})
    public void CreateRoute(){
        Response response = createRouteResponse();
        response.then().statusCode(201);
        String routeId = findValueThroughKeyResponse("routeId", response);
        apiClient.DELETERouteById(routeId);
    }

    @Test(groups = {"API"})
    public void GetRoute(){
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";
        Response response = apiClient.GETRoutes(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetRoutesById(){
        Response response = createRouteResponse();
        String routeId = findValueThroughKeyResponse("routeId", response);
        response = apiClient.GETRoutesById(routeId);
        response.then().statusCode(200);
        apiClient.DELETERouteById(routeId);
    }

    @Test(groups = {"API"})
    public void PUTRoutesById(){
        Map<String, Object> routeData = routeDataInfo();
        Response response = apiClient.POSTCreateRoute((String) routeData.get("routeName"),(List<Object>)routeData.get("routesList"),
                (String) routeData.get("visibilityLevel"), (String)routeData.get("organizationId"));
        String routeId = findValueThroughKeyResponse("routeId", response);
        Object routeSegments = routeData.get("routesList");
        routeData.remove("routesList");
        routeData.put("routeSegments", routeSegments);
        routeData.put("routeId", routeId);
        routeData.replace("routeName", "AutoAPIRoute" + Instant.now().toEpochMilli());
        routeData.remove("organizationId");
        response = apiClient.PUTRoutesById(routeId, routeData);
        response.then().statusCode(200);
        apiClient.DELETERouteById(routeId);
    }

    @Test(groups = {"API"})
    public void DeleteRouteById(){
        Response response = createRouteResponse();
        String routeId = findValueThroughKeyResponse("routeId", response);
        response = apiClient.DELETERouteById(routeId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetRouteExport(){
        Response response = apiClient.GETRoutesExport();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void DeleteRoutesBulk(){
        int numberOfRoutes = 5;
        List<String> listOfRoutesId = new ArrayList<>();
        for(int i = 0; i < numberOfRoutes; i++){
            Response createRouteResponse = createRouteResponse();
            String routeId = findValueThroughKeyResponse("routeId", createRouteResponse);
            listOfRoutesId.add(routeId);
        }
        Response deleteResponse = apiClient.DELETERoutesBulkDelete(listOfRoutesId);
        deleteResponse.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDeviceProfiles(){
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETDeviceProfiles(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    public Map<String, Object> deviceProfileInfo() {
        Map<String, Object> deviceProfileData = new HashMap<>(Map.of(
                "deviceProfileName", "AutoAPIDeviceProfile" + Instant.now().toEpochMilli(),
                "deviceTypeId", "B6C1FCC6-EEC5-47B7-88E0-626DB35EDE87",
                "minTemperatureThreshold", 0,
                "maxTemperatureThreshold", 2,
                "recordingInterval", 1,
                "reportingInterval", 1,
                "visibilityLevel", "OrganizationOnly",
                "organizationId", Config.APIOrgId
        ));
        return deviceProfileData; // Now you can modify this map
    }

    public Response createDeviceProfileResponse(){
        Map<String, Object> deviceProfileData = deviceProfileInfo();
        Response response = apiClient.POSTCreateDeviceProfile((String)deviceProfileData.get("deviceProfileName"),
                (String)deviceProfileData.get("deviceTypeId"),(int)deviceProfileData.get("minTemperatureThreshold"),
                (int)deviceProfileData.get("maxTemperatureThreshold"), (int)deviceProfileData.get("recordingInterval"),
                (int)deviceProfileData.get("reportingInterval"), (String)deviceProfileData.get("visibilityLevel"),
                (String)deviceProfileData.get("organizationId"));
        return response;
    }

    @Test(groups = {"API"})
    public void createDeviceProfile(){
        Response response = createDeviceProfileResponse();
        response.then().statusCode(201);
        String deviceProfileId = findValueThroughKeyResponse("deviceProfileId", response);
        apiClient.DELETEDeviceProfileById(deviceProfileId);
    }

    @Test(groups = {"API"})
    public void GetDeviceProfileExport(){
        Response response = apiClient.GETDeviceProfileExport();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDeviceProfileById(){
        Response response = createDeviceProfileResponse();
        String deviceProfileId = findValueThroughKeyResponse("deviceProfileId", response);
        response = apiClient.GETDeviceProfileById(deviceProfileId);
        response.then().statusCode(200);
        apiClient.DELETEDeviceProfileById(deviceProfileId);
    }

    @Test(groups = {"API"})
    public void PutDeviceProfileByDeviceProfileId(){
        Map<String, Object> deviceProfileData = deviceProfileInfo();
        Response response = apiClient.POSTCreateDeviceProfile((String)deviceProfileData.get("deviceProfileName"),
                (String)deviceProfileData.get("deviceTypeId"),(int)deviceProfileData.get("minTemperatureThreshold"),
                (int)deviceProfileData.get("maxTemperatureThreshold"), (int)deviceProfileData.get("recordingInterval"),
                (int)deviceProfileData.get("reportingInterval"), (String)deviceProfileData.get("visibilityLevel"),
                (String)deviceProfileData.get("organizationId"));
        String deviceProfileId = findValueThroughKeyResponse("deviceProfileId", response);
        deviceProfileData.remove("organizationId");
        deviceProfileData.replace("deviceProfileName", "AutoAPIDeviceProfile" + Instant.now().toEpochMilli());
        deviceProfileData.put("deviceProfileId", deviceProfileId);
        response = apiClient.PUTDeviceProfileById(deviceProfileId, deviceProfileData);
        response.then().statusCode(200);
        apiClient.DELETEDeviceProfileById(deviceProfileId);
    }

    @Test(groups = {"API"})
    public void DeleteDeviceProfiles(){
        Response response = createDeviceProfileResponse();
        String deviceProfileId = findValueThroughKeyResponse("deviceProfileId", response);
        response = apiClient.DELETEDeviceProfileById(deviceProfileId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetAssignedDeviceByDeviceProfileId(){
        Response response = apiClient.GETAssignedDevicesDeviceProfileById("8863c9cb-dbe5-441f-8065-538658f993a5");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GETAssignedDevicesExportWithSpecificDeviceProfileId(){
        Response response = apiClient.GETAssignedDevicesExportDeviceProfileById("8863c9cb-dbe5-441f-8065-538658f993a5");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDevices(){
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETDevices(pageNumber, pageSize, sortColumns, sortDirections, null);
        response.then().statusCode(200);
    }

    public Response createTradoglapanProDeviceResponse(){
        Map<String, String> tradoglapanProDetails = tradoglapanProDetailsInfo();
        return apiClient.POSTCreateDevice(tradoglapanProDetails.get("deviceName"), tradoglapanProDetails.get("deviceTypeId"),
                tradoglapanProDetails.get("deviceProfileId"), tradoglapanProDetails.get("organizationId"));
    }

    @Test(groups = {"API"})
    public void PostDevices(){
        Response response = createTradoglapanProDeviceResponse();
        response.then().statusCode(201);
        String deviceId = findValueThroughKeyResponse("deviceId", response);
        apiClient.DELETEDeviceById(deviceId);
    }

    // Todo: patch device by deviceId

    @Test(groups = {"API"})
    public void DeleteDevices(){
        Response response = createTradoglapanProDeviceResponse();
        String deviceId = findValueThroughKeyResponse("deviceId", response);
        response = apiClient.DELETEDeviceById(deviceId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetDeviceById(){
        Response response = createTradoglapanProDeviceResponse();
        String deviceId = findValueThroughKeyResponse("deviceId", response);
        response = apiClient.GETDeviceById(deviceId);
        response.then().statusCode(200);
        apiClient.DELETEDeviceById(deviceId);
    }

    @Test(groups = {"API"})
    public void GetAssetBindingHistoryByDeviceId(){
        Response response = apiClient.GETDeviceAssetBindingHistoryByDeviceId("b8bf1411-2f37-4c01-ab94-0fe38b184935", "1", "20");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GETDeviceExport(){
        Response response = apiClient.GETDeviceExport();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetAssetBindingHistoryExportByDeviceId(){
        Response response = apiClient.GETDeviceAssetBindingHistoryExportByDeviceId("b8bf1411-2f37-4c01-ab94-0fe38b184935", "1", "20");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void DeleteDevicesBulk(){
        int numberOfDevices = 5;
        List<String> listOfDevicesId = new ArrayList<>();
        for(int i = 0; i < numberOfDevices; i++){
            Response createdeviceResponse = createTradoglapanProDeviceResponse();
            String deviceId = findValueThroughKeyResponse("deviceId", createdeviceResponse);
            listOfDevicesId.add(deviceId);
        }
        Response deleteResponse = apiClient.DELETEDevicesBulkDelete(listOfDevicesId);
        deleteResponse.then().statusCode(200);
    }

    // Todo: get device export
    // Todo: get assetbindinghistoryby deviceid export
    // Todo: delete device by bulk
    // Todo: post device bulk import

    @Test(groups = {"API"})
    public void assetList(){
        String pageNumber = "1";
        String pageSize = "20";
        String sortColumns = "UpdatedAt";
        String sortDirections = "desc";

        Response response = apiClient.GETAssets(pageNumber, pageSize, sortColumns, sortDirections, null);
        // Validate the response
        response.then().statusCode(200);
        response.prettyPrint();
    }

    public Response createAssetdoglapanRKNResponse(){
        Map<String, Object> assetData = assetDatadoglapanRKN();
        Response response = apiClient.POSTCreateAsset((String) assetData.get("assetId"), (String) assetData.get("assetName"),
                (String) assetData.get("assetTypeId"), (String) assetData.get("organizationId"), (String) assetData.get("visibilityLevel"));
        return response;
    }

    @Test(groups = {"API"})
    public void createAsset(){
        Response response = createAssetdoglapanRKNResponse();
        response.then().statusCode(201);
        String assetId = findValueThroughKeyResponse("assetId", response);
        response = apiClient.DELETEAssetById(assetId);
    }

    @Test(groups = {"API"})
    public void assetDetailsById(){
        //Response response = createAssetdoglapanRKNResponse();
        String assetId = "c94ef969-7eb5-47e7-a42d-66fe207deb90";//findValueThroughKeyResponse("assetId", response);
        Response response = apiClient.GETAssetByAssetId(assetId);
        response.then().statusCode(200);
        //response = apiClient.DELETEAssetById(assetId);
    }

    // Todo: patch assets by assetid

    @Test(groups = {"API"})
    public void deleteAssetById(){
        Response response = createAssetdoglapanRKNResponse();
        String assetId = findValueThroughKeyResponse("assetId", response);
        response = apiClient.DELETEAssetById(assetId);
        response.then().statusCode(200);
    }

    // Todo: post asset external

    @Test(groups = {"API"})
    public void PostUnbindDeviceByAssetId(){
        Response response = createAssetdoglapanRKNResponse();
        String assetId = findValueThroughKeyResponse("assetId", response);
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device1 = deviceIdMountSlotIdTradoglapanPro();
        devices.add(device1);
        apiClient.POSTAssetBindByAssetId(assetId, devices);
        List<String> devicesId = new ArrayList<>();
        for(int i = 0; i < devices.size(); i++){
            devicesId.add((String) devices.get(i).get("deviceId"));
        }
        response = apiClient.POSTUnBindDevice(assetId, devicesId);
        response.then().statusCode(200);
        apiClient.DELETEAssetById(assetId);
        for(int i = 0; i < devicesId.size(); i++){
            apiClient.DELETEDeviceById(devicesId.get(i));
        }
    }

    @Test(groups = {"API"})
    public void BoundDeviceListByAssetId(){
        Response response = createAssetdoglapanRKNResponse();
        String assetId = findValueThroughKeyResponse("assetId", response);
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device1 = deviceIdMountSlotIdTradoglapanPro();
        devices.add(device1);
        apiClient.POSTAssetBindByAssetId(assetId, devices);
        response = apiClient.GETAssetsBoundDevices(assetId);
        response.then().statusCode(200);
        List<String> devicesId = new ArrayList<>();
        for(int i = 0; i < devices.size(); i++){
            devicesId.add((String) devices.get(i).get("deviceId"));
        }
        apiClient.POSTUnBindDevice(assetId, devicesId);
        apiClient.DELETEAssetById(assetId);
        for(int i = 0; i < devicesId.size(); i++){
            apiClient.DELETEDeviceById(devicesId.get(i));
        }
    }

    @Test(groups = {"API"})
    public void PostBindAssetsByAssetId(){
        Response response = createAssetdoglapanRKNResponse();
        String assetId = findValueThroughKeyResponse("assetId", response);
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device1 = deviceIdMountSlotIdTradoglapanPro();
        devices.add(device1);
        response = apiClient.POSTAssetBindByAssetId(assetId, devices);
        response.then().statusCode(200);
        List<String> devicesId = new ArrayList<>();
        for(int i = 0; i < devices.size(); i++){
            devicesId.add((String) devices.get(i).get("deviceId"));
        }
        apiClient.POSTUnBindDevice(assetId, devicesId);
        apiClient.DELETEAssetById(assetId);
        for(int i = 0; i < devicesId.size(); i++){
            apiClient.DELETEDeviceById(devicesId.get(i));
        }
    }

    // Todo: PostBindUnbinAssetsByAssetId

    @Test(groups = {"API"})
    public void PostBindUnbindAssetsByAssetId(){
        Response response = createAssetdoglapanRKNResponse();
        String assetId = findValueThroughKeyResponse("assetId", response);
        response = createTradoglapanProDeviceResponse();
        String device1Id = findValueThroughKeyResponse("deviceId", response);
        String device1Name = findValueThroughKeyResponse("deviceName", apiClient.GETDeviceById(device1Id));
        Map<String, String>device1Details = new HashMap<>();
        device1Details.put("deviceName", device1Name);
        device1Details.put("bindingAction", "Bind");
        List<Map<String, String>> listOfDevices = new ArrayList<>();
        listOfDevices.add(device1Details);
        Map<String, Object> bindUnbindScenario = new HashMap<>();
        bindUnbindScenario.put("assetId", assetId);
        bindUnbindScenario.put("deviceBindings", listOfDevices);
        response = apiClient.POSTBindUnbind(assetId, bindUnbindScenario);
        response.then().statusCode(200);
        device1Details.replace("bindingAction", "Unbind");
        listOfDevices.remove(0);
        listOfDevices.add(device1Details);
        bindUnbindScenario.replace("deviceBindings", listOfDevices);
        System.out.println(bindUnbindScenario);
        response = apiClient.POSTBindUnbind(assetId, bindUnbindScenario);
        response.then().statusCode(200);
        apiClient.DELETEDeviceById(device1Id);
        apiClient.DELETEAssetById(assetId);
    }

    @Test(groups = {"API"})
    public void GetAssetSensorTypesByAssetId(){
        Response response = apiClient.GETAssetSensorType("7c5b2a85-f58c-4821-aa14-a47b0e6c4877");
        response.then().statusCode(200);
    }

    // Todo: PostAssetsOrganizationByAssetId
    @Test(groups = {"API"})
    public void GetAssetDeviceBindingHistoryByAssetId(){
        Response response = apiClient.GETAssetDeviceBindingHistory("7c5b2a85-f58c-4821-aa14-a47b0e6c4877");
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetAssetsExport(){
        Response response = apiClient.GETAssetsExport();
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void GetAssetsOverview(){
        Response response = apiClient.GETAssetsOverview();
        response.then().statusCode(200);
    }
    // Todo: PostAssetsBulkImport
    @Test(groups = {"API"})
    public void DeleteAssetsBulk(){
        int numberOfAssets = 5;
        List<String> listOfAssetsId = new ArrayList<>();
        for(int i = 0; i < numberOfAssets; i++){
            Response createAssetResponse = createAssetdoglapanRKNResponse();
            String assetId = findValueThroughKeyResponse("assetId", createAssetResponse);
            listOfAssetsId.add(assetId);
        }
        Response deleteResponse = apiClient.DELETEAssetsBulkDelete(listOfAssetsId);
        deleteResponse.then().statusCode(200);
    }
    // Todo: PostAssetsBulkBinding
    // Todo: PostAssetsBulkUnbinding

    // Todo: shipments all

    // ====================================================================
    public Map<String, Object> BulkAirCargoShipmentWithMultipleAssetData(){
        long time = Instant.now().toEpochMilli();
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
        String emails = "auto@doglapanglobal.com,autotest@doglapanglobal.com";

        String plannedArrivalDate = "2026-12-03T17:50:00.000Z";
        String plannedDepartureDate = "2026-12-02T17:50:00.000Z";

        String organizationId = "49194360-27AB-4434-991F-88116DC775BB";

        List<String> ruleIds = new ArrayList<>();
        ruleIds.add("30E138F0-AD19-4AC4-A520-8358B9523704");

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
        Map<String, Object> shipmentData = new HashMap<>();
        shipmentData.put("shipmentName", shipmentName);
        shipmentData.put("shipperName", shipperName);
        shipmentData.put("orderNumber", orderNumber);
        shipmentData.put("carriedId", CarrierId);
        shipmentData.put("travelMode", travelMode);
        shipmentData.put("customerName", customerName);
        shipmentData.put("recipientName", recipientName);
        shipmentData.put("shipmentType", shipmentType);
        shipmentData.put("shipmentDirection", shipmentDirection);
        shipmentData.put("airwayBillNumber", airwayBillNumber);
        shipmentData.put("emails", emails);
        shipmentData.put("plannedDepartureDate", plannedDepartureDate);
        shipmentData.put("plannedArrivalDate", plannedArrivalDate);
        shipmentData.put("organizationId", organizationId);
        shipmentData.put("ruleIds", ruleIds);
        shipmentData.put("deviceProfileId", deviceProfileId);
        shipmentData.put("routeOrLocationChoice", routeOrLocationChoice);
        shipmentData.put("isDynamicOriginLocation", isDynamicOriginLocation);
        shipmentData.put("dynamicOriginLocation", dynamicOriginLocation);
        shipmentData.put("isDynamicDestinationLocation", isDynamicDestinationLocation);
        shipmentData.put("dynamicDestinationLocation", dynamicDestinationLocation);
        shipmentData.put("routeId", routeId);
        shipmentData.put("originLocationId", originLocationId);
        shipmentData.put("destinationLocationId", destinationLocationId);
        shipmentData.put("assets", assets);
        return shipmentData;
    }

    @Test(groups = {"API"})
    public void shipmentDetailsById(){
        Map<String, Object> shipmentData = BulkAirCargoShipmentWithMultipleAssetData();
        Response response = apiClient.POSTShipmentsCreate((String)shipmentData.get("shipmentName"), (String)shipmentData.get("shipperName"),
                (String)shipmentData.get("orderNumber"), (String)shipmentData.get("carriedId"), (String)shipmentData.get("travelMode"),
                (String)shipmentData.get("customerName"), (String)shipmentData.get("recipientName"), (String)shipmentData.get("shipmentType"),
                (String)shipmentData.get("shipmentDirection"), (String)shipmentData.get("airwayBillNumber"),
                (String)shipmentData.get("emails"), (String)shipmentData.get("plannedDepartureDate"),
                (String)shipmentData.get("plannedArrivalDate"), (String)shipmentData.get("organizationId"),
                (List<String>) shipmentData.get("ruleIds"), (String)shipmentData.get("deviceProfileId"), (String)shipmentData.get("routeOrLocationChoice"),
                (boolean)shipmentData.get("isDynamicOriginLocation"), (Map<String, Object>)shipmentData.get("dynamicOriginLocation"),
                (boolean)shipmentData.get("isDynamicDestinationLocation"), (Map<String, Object>)shipmentData.get("dynamicDestinationLocation"),
                (String)shipmentData.get("routeId"), (String)shipmentData.get("originLocationId"),
                (String)shipmentData.get("destinationLocationId"), (List<Map<String, String>>)shipmentData.get("assets"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String shipmentId = jsonResponse.getString("shipmentId");
        response = apiClient.GETShipmentByShipmentId(shipmentId);
        response.then().statusCode(200);
        response.prettyPrint();
        response = apiClient.DELETEShipmentById(shipmentId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void deleteShipmentById(){
        Map<String, Object> shipmentData = BulkAirCargoShipmentWithMultipleAssetData();
        Response response = apiClient.POSTShipmentsCreate((String)shipmentData.get("shipmentName"), (String)shipmentData.get("shipperName"),
                (String)shipmentData.get("orderNumber"), (String)shipmentData.get("carriedId"), (String)shipmentData.get("travelMode"),
                (String)shipmentData.get("customerName"), (String)shipmentData.get("recipientName"), (String)shipmentData.get("shipmentType"),
                (String)shipmentData.get("shipmentDirection"), (String)shipmentData.get("airwayBillNumber"),
                (String)shipmentData.get("emails"), (String)shipmentData.get("plannedDepartureDate"),
                (String)shipmentData.get("plannedArrivalDate"), (String)shipmentData.get("organizationId"),
                (List<String>) shipmentData.get("ruleIds"), (String)shipmentData.get("deviceProfileId"), (String)shipmentData.get("routeOrLocationChoice"),
                (boolean)shipmentData.get("isDynamicOriginLocation"), (Map<String, Object>)shipmentData.get("dynamicOriginLocation"),
                (boolean)shipmentData.get("isDynamicDestinationLocation"), (Map<String, Object>)shipmentData.get("dynamicDestinationLocation"),
                (String)shipmentData.get("routeId"), (String)shipmentData.get("originLocationId"),
                (String)shipmentData.get("destinationLocationId"), (List<Map<String, String>>)shipmentData.get("assets"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String shipmentId = jsonResponse.getString("shipmentId");
        response = apiClient.DELETEShipmentById(shipmentId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void createShipment(){
        Map<String, Object> shipmentData = BulkAirCargoShipmentWithMultipleAssetData();
        Response response = apiClient.POSTShipmentsCreate((String)shipmentData.get("shipmentName"), (String)shipmentData.get("shipperName"),
                (String)shipmentData.get("orderNumber"), (String)shipmentData.get("carriedId"), (String)shipmentData.get("travelMode"),
                (String)shipmentData.get("customerName"), (String)shipmentData.get("recipientName"), (String)shipmentData.get("shipmentType"),
                (String)shipmentData.get("shipmentDirection"), (String)shipmentData.get("airwayBillNumber"),
                (String)shipmentData.get("emails"), (String)shipmentData.get("plannedDepartureDate"),
                (String)shipmentData.get("plannedArrivalDate"), (String)shipmentData.get("organizationId"),
                (List<String>) shipmentData.get("ruleIds"), (String)shipmentData.get("deviceProfileId"), (String)shipmentData.get("routeOrLocationChoice"),
                (boolean)shipmentData.get("isDynamicOriginLocation"), (Map<String, Object>)shipmentData.get("dynamicOriginLocation"),
                (boolean)shipmentData.get("isDynamicDestinationLocation"), (Map<String, Object>)shipmentData.get("dynamicDestinationLocation"),
                (String)shipmentData.get("routeId"), (String)shipmentData.get("originLocationId"),
                (String)shipmentData.get("destinationLocationId"), (List<Map<String, String>>)shipmentData.get("assets"));
        response.then().statusCode(201);
        response.prettyPrint();
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String shipmentId = jsonResponse.getString("shipmentId");
        response = apiClient.DELETEShipmentById(shipmentId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void shipmentList(){
        Response response = apiClient.GETShipments("1", "20", "UpdatedAt", "desc");
        response.then().statusCode(200);
        response.prettyPrint();
    }

    @Test(groups = {"API"})
    public void shipmentSummaryByID(){
        Map<String, Object> shipmentData = BulkAirCargoShipmentWithMultipleAssetData();
        Response response = apiClient.POSTShipmentsCreate((String)shipmentData.get("shipmentName"), (String)shipmentData.get("shipperName"),
                (String)shipmentData.get("orderNumber"), (String)shipmentData.get("carriedId"), (String)shipmentData.get("travelMode"),
                (String)shipmentData.get("customerName"), (String)shipmentData.get("recipientName"), (String)shipmentData.get("shipmentType"),
                (String)shipmentData.get("shipmentDirection"), (String)shipmentData.get("airwayBillNumber"),
                (String)shipmentData.get("emails"), (String)shipmentData.get("plannedDepartureDate"),
                (String)shipmentData.get("plannedArrivalDate"), (String)shipmentData.get("organizationId"),
                (List<String>) shipmentData.get("ruleIds"), (String)shipmentData.get("deviceProfileId"), (String)shipmentData.get("routeOrLocationChoice"),
                (boolean)shipmentData.get("isDynamicOriginLocation"), (Map<String, Object>)shipmentData.get("dynamicOriginLocation"),
                (boolean)shipmentData.get("isDynamicDestinationLocation"), (Map<String, Object>)shipmentData.get("dynamicDestinationLocation"),
                (String)shipmentData.get("routeId"), (String)shipmentData.get("originLocationId"),
                (String)shipmentData.get("destinationLocationId"), (List<Map<String, String>>)shipmentData.get("assets"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String shipmentId = jsonResponse.getString("shipmentId");
        response = apiClient.GETShipmentSummaryByShipmentId(shipmentId);
        response.then().statusCode(200);
        response.prettyPrint();
        response = apiClient.DELETEShipmentById(shipmentId);
        response.then().statusCode(200);
    }

    @Test(groups = {"API"})
    public void shipmentCompleteByID(){
        Map<String, Object> shipmentData = BulkAirCargoShipmentWithMultipleAssetData();
        Response response = apiClient.POSTShipmentsCreate((String)shipmentData.get("shipmentName"), (String)shipmentData.get("shipperName"),
                (String)shipmentData.get("orderNumber"), (String)shipmentData.get("carriedId"), (String)shipmentData.get("travelMode"),
                (String)shipmentData.get("customerName"), (String)shipmentData.get("recipientName"), (String)shipmentData.get("shipmentType"),
                (String)shipmentData.get("shipmentDirection"), (String)shipmentData.get("airwayBillNumber"),
                (String)shipmentData.get("emails"), (String)shipmentData.get("plannedDepartureDate"),
                (String)shipmentData.get("plannedArrivalDate"), (String)shipmentData.get("organizationId"),
                (List<String>) shipmentData.get("ruleIds"), (String)shipmentData.get("deviceProfileId"), (String)shipmentData.get("routeOrLocationChoice"),
                (boolean)shipmentData.get("isDynamicOriginLocation"), (Map<String, Object>)shipmentData.get("dynamicOriginLocation"),
                (boolean)shipmentData.get("isDynamicDestinationLocation"), (Map<String, Object>)shipmentData.get("dynamicDestinationLocation"),
                (String)shipmentData.get("routeId"), (String)shipmentData.get("originLocationId"),
                (String)shipmentData.get("destinationLocationId"), (List<Map<String, String>>)shipmentData.get("assets"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String shipmentId = jsonResponse.getString("shipmentId");
        response = apiClient.POSTShipmentCompleteByShipmentId(shipmentId);
        response.then().statusCode(200);
    }

    public Map<String, Object> assetDatadoglapanRKN(){
        long time = Instant.now().toEpochMilli();
        String assetName = "AutoAPIAsset" + time;
        String assetTypeId = "07B138D3-F7A8-437C-8511-ADF933855C28";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        String visibilityLevel = "OrganizationOnly";
        Map<String, Object> assetData = new HashMap<>();
        assetData.put("assetName", assetName);
        assetData.put("assetTypeId", assetTypeId);
        assetData.put("organizationId", organizationId);
        assetData.put("visibilityLevel", visibilityLevel);
        return assetData;
    }

    public Map<String, String> tradoglapanProDetailsInfo(){
        long time = Instant.now().toEpochMilli();
        String DeviceName = "AutoDevice"+time;
        String deviceTypeID = "B6C1FCC6-EEC5-47B7-88E0-626DB35EDE87";
        String deviceProfileID = "AC526773-84D2-445A-B17A-E46982AA5259";
        String OrganizationID = "49194360-27AB-4434-991F-88116DC775BB";
        Map<String, String> deviceInfo = new HashMap<>();
        deviceInfo.put("deviceName", DeviceName);
        deviceInfo.put("deviceTypeId", deviceTypeID);
        deviceInfo.put("deviceProfileId", deviceProfileID);
        deviceInfo.put("organizationId", OrganizationID);
        return deviceInfo;
    }

    public Map<String, Object> deviceIdMountSlotIdTradoglapanPro(){
        Map<String, String> tradoglapanProDetails = tradoglapanProDetailsInfo();
        Response response = apiClient.POSTCreateDevice(tradoglapanProDetails.get("deviceName"), tradoglapanProDetails.get("deviceTypeId"),
                tradoglapanProDetails.get("deviceProfileId"), tradoglapanProDetails.get("organizationId"));
        String deviceId = findValueThroughKeyResponse("deviceId", response);
        String mountSlotId = "";
        Map<String, Object> deviceDetails = new HashMap<>();
        deviceDetails.put("deviceId",deviceId);
        if(!mountSlotId.isEmpty())deviceDetails.put("mountSlotId", mountSlotId);
        return deviceDetails;
    }

    @Test(groups = {"API"})
    public void bindDeviceByAssetId(){
        Map<String, Object> assetData = assetDatadoglapanRKN();
        Response response = apiClient.POSTCreateAsset((String) assetData.get("assetId"), (String) assetData.get("assetName"),
                (String) assetData.get("assetTypeId"), (String) assetData.get("organizationId"), (String) assetData.get("visibilityLevel"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String assetId = jsonResponse.getString("assetId");
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device1 = deviceIdMountSlotIdTradoglapanPro();
        devices.add(device1);
        response = apiClient.POSTAssetBindByAssetId(assetId, devices);
        response.prettyPrint();
        response.then().statusCode(200);
        List<String> devicesId = new ArrayList<>();
        for(int i = 0; i < devices.size(); i++){
            devicesId.add((String) devices.get(i).get("deviceId"));
        }
        response = apiClient.POSTUnBindDevice(assetId, devicesId);
        response.then().statusCode(200);
        response = apiClient.DELETEAssetById(assetId);
        response.then().statusCode(200);
        for(int i = 0; i < devicesId.size(); i++){
            response = apiClient.DELETEDeviceById(devicesId.get(i));
            response.then().statusCode(200);
        }
    }

    @Test(groups = {"API"})
    public void UnboundDeviceListByAssetId(){
        Map<String, Object> assetData = assetDatadoglapanRKN();
        Response response = apiClient.POSTCreateAsset((String) assetData.get("assetId"), (String) assetData.get("assetName"),
                (String) assetData.get("assetTypeId"), (String) assetData.get("organizationId"), (String) assetData.get("visibilityLevel"));
        response.then().statusCode(201);
        String jsonString = response.body().asString();
        JSONObject jsonResponse = new JSONObject(jsonString);
        String assetId = jsonResponse.getString("assetId");
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> device1 = deviceIdMountSlotIdTradoglapanPro();
        devices.add(device1);
        response = apiClient.POSTAssetBindByAssetId(assetId, devices);
        response.then().statusCode(200);
        response.prettyPrint();
        response.then().statusCode(200);
        List<String> devicesId = new ArrayList<>();
        for(int i = 0; i < devices.size(); i++){
            devicesId.add((String) devices.get(i).get("deviceId"));
        }
        response = apiClient.POSTUnBindDevice(assetId, devicesId);
        response.then().statusCode(200);
        response = apiClient.GETAssetsUnboundDevices(assetId, "1", "20", "UpdatedAt", "desc");
        response.prettyPrint();
        response.then().statusCode(200);
        response = apiClient.DELETEAssetById(assetId);
        response.then().statusCode(200);
        for(int i = 0; i < devicesId.size(); i++){
            response = apiClient.DELETEDeviceById(devicesId.get(i));
            response.then().statusCode(200);
        }
    }

    // Todo: GetSearchedRules

    public void CreateDeviceCreateAssetAndBind(){
        long time = Instant.now().toEpochMilli();
        String assetId = ""; // Can be empty as per the curl
        String assetName = "AutoAPIAsset" + time;
        String assetTypeId = "226ED976-92F6-4E1F-87B5-9962779CD4C6";
        String visibilityLevel = "OrganizationOnly";
        String DeviceName = "AutoAPIDevice" + time;
        String deviceTypeID = "B6C1FCC6-EEC5-47B7-88E0-626DB35EDE87"; //tradoglapanpro
        String deviceProfileID = "96CF238F-2278-4625-B558-CC8E9A43983A";
        String OrganizationID = "49194360-27AB-4434-991F-88116DC775BB";
        String deviceId = "";
        String mountSlotId = null;
        List<Map<String, Object>> devices = new ArrayList<>();
        Map<String, Object> deviceDetails = new HashMap<>();

        //create Asset
        Response response = apiClient.POSTCreateAsset(assetId, assetName, assetTypeId, OrganizationID, visibilityLevel);
        response.prettyPrint();
        response.then().statusCode(201); // Assuming 201 Created is the expected status code
        String jsonString = response.body().asString(); // Response ke string e convert korlam
        JSONObject jsonResponse = new JSONObject(jsonString); // JSON Object e convert
        assetId = jsonResponse.getString("assetId"); // "name" field er value
        //Create Device
        response = apiClient.POSTCreateDevice(DeviceName, deviceTypeID, deviceProfileID, OrganizationID);
        response.then().statusCode(201);
        jsonString = response.body().asString();
        jsonResponse = new JSONObject(jsonString);
        deviceId = jsonResponse.getString("deviceId");
        //Bound Asset with Device
        deviceDetails.put("deviceId",deviceId);
        //       deviceDetails.put("mountSlotId", mountSlotId);
        devices.add(deviceDetails);
        response = apiClient.POSTAssetBindByAssetId(assetId, devices);
        response.then().statusCode(200);
    }
    public List<String> createAssetNeed(String assetTypeId, String assettypename){
        long time = Instant.now().toEpochMilli();
        String assetName = "AutoAPIAsset" + time;
        //String assetTypeId = "0BF49523-99BE-416B-9059-10C58CF042B6";
        String organizationId = "49194360-27ab-4434-991f-88116dc775bb";
        String visibilityLevel = "OrganizationOnly";
        Map<String, Object> assetData = new HashMap<>();
        assetData.put("assetName", assetName);
        assetData.put("assetTypeId", assetTypeId);
        assetData.put("organizationId", organizationId);
        assetData.put("visibilityLevel", visibilityLevel);
        Response response = apiClient.POSTCreateAsset((String) assetData.get("assetId"), (String) assetData.get("assetName"),
                (String) assetData.get("assetTypeId"), (String) assetData.get("organizationId"), (String) assetData.get("visibilityLevel"));
        response.then().statusCode(201);
        String assetId = findValueThroughKeyResponse("assetId", response);
        List<String> ab = new ArrayList<>();
        ab.add("\""+assettypename+"\"");
        ab.add("\""+assetId+"\"");
        ab.add("\""+assetName+"\"");
        return ab;
    }
    public void task() throws SQLException {
        List<List<String>> pqr = new ArrayList<>();
        String query = "select assettypeid, assettypename from assettypes";
        ResultSet rs = DBUtility.executeQuery(query, test);
        List<List<String>> tableData = convertResultSetToList(rs);
        for(int i = 1; i < tableData.size(); i++) {
            for (int j = 0; j < 3; j++) {
                List<String> abs = createAssetNeed(tableData.get(i).get(0), tableData.get(i).get(1));
                pqr.add(abs);
            }
        }
        System.out.println(pqr);
    }

    public static ThreadLocal<WebDriver> driver1 = new ThreadLocal<>();

    public static String accessToken() {
        driver1.set(new ChromeDriver());
        WebDriver driver = driver1.get();

        driver.get("https://TEST-frontend-test-cyhseggcd6h3czdj.eastus-01.azurewebsites.net/?orgId=23080");

        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        AtomicReference<String> accessToken = new AtomicReference<>(null);

        devTools.addListener(Network.responseReceived(), response -> {
            String requestUrl = response.getResponse().getUrl();
            if (requestUrl.contains("/api/auth/session")) {
                System.out.println("Session API Response Found: " + requestUrl);
                try {
                    String responseBody = (String) ((JavascriptExecutor) driver).executeScript("return fetch(arguments[0]).then(res => res.json()).then(data => JSON.stringify(data))", requestUrl);
                    System.out.println("Response Body: " + responseBody);
                    if (responseBody.contains("accessToken")) {
                        String token = responseBody.split("\"accessToken\":\"")[1].split("\"")[0];
                        accessToken.set(token);
                        System.out.println("Extracted Access Token: " + token);
                    }
                } catch (Exception e) {
                    System.out.println("Error extracting Access Token: " + e.getMessage());
                }
            }
        });
        LoginPage loginPage = new LoginPage(driver);
        String email = loginPage.email;
        String password = loginPage.password;
        DashboardPage dashboardPage = loginPage.login(email, password);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();
        return accessToken.get();
    }

    @Test
    public void PostAuthLogout(){
        Response response = apiClient.POSTAuthLogout();
        System.out.println(response.prettyPrint());
        response.then().statusCode(200);
    }

}
