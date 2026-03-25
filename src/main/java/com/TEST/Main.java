package com.TEST;

import org.json.JSONArray;
import org.json.JSONObject;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
//    public static void main(String[] args) {
//        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
//        // to see how IntelliJ IDEA suggests fixing it.
//        System.out.printf("Hello and welcome!");
//
//        for (int i = 1; i <= 5; i++) {
//            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
//            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
//            System.out.println("i = " + i);
//        }
//    }

    public static void main(String[] args){

//        ========================= Using Jackson ================================
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Create the root object
//        ObjectNode payload = mapper.createObjectNode();
//        payload.put("ruleSetName", "newRule0124202501");
//        payload.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
//        payload.put("emails", "test@example.com");
//        payload.put("visibilityLevel", "OrganizationOnly");
//
//        // Create the sensorRuleItems array
//        ArrayNode sensorRuleItems = mapper.createArrayNode();
//
//        // Add items to the array
//        ObjectNode sensor1 = mapper.createObjectNode();
//        sensor1.put("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
//        sensor1.put("sensorTypeName", "probeTemp");
//        sensor1.put("isEnabled", true);
//        sensor1.put("minThreshold", "5");
//        sensor1.put("maxThreshold", "10");
//        sensor1.put("exceptionDuration", "1");
//        sensorRuleItems.add(sensor1);
//
//        ObjectNode sensor2 = mapper.createObjectNode();
//        sensor2.put("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
//        sensor2.put("sensorTypeName", "deviceTemp");
//        sensor2.put("isEnabled", true);
//        sensor2.put("minThreshold", "5");
//        sensor2.put("maxThreshold", "10");
//        sensor2.put("exceptionDuration", "2");
//        sensorRuleItems.add(sensor2);
//
//        // Add more sensors similarly...
//
//        // Attach the array to the root object
//        payload.set("sensorRuleItems", sensorRuleItems);
//
//        // Convert to JSON string
//        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
//        System.out.println(jsonString);

//        ========================== Using Gson ================================
//        // Create the root object
//        JsonObject payload = new JsonObject();
//        payload.addProperty("ruleSetName", "newRule0124202501");
//        payload.addProperty("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
//        payload.addProperty("emails", "test@example.com");
//        payload.addProperty("visibilityLevel", "OrganizationOnly");
//
//        // Create the sensorRuleItems array
//        JsonArray sensorRuleItems = new JsonArray();
//
//        // Add items to the array
//        JsonObject sensor1 = new JsonObject();
//        sensor1.addProperty("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
//        sensor1.addProperty("sensorTypeName", "probeTemp");
//        sensor1.addProperty("isEnabled", true);
//        sensor1.addProperty("minThreshold", "5");
//        sensor1.addProperty("maxThreshold", "10");
//        sensor1.addProperty("exceptionDuration", "1");
//        sensorRuleItems.add(sensor1);
//
//        JsonObject sensor2 = new JsonObject();
//        sensor2.addProperty("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
//        sensor2.addProperty("sensorTypeName", "deviceTemp");
//        sensor2.addProperty("isEnabled", true);
//        sensor2.addProperty("minThreshold", "5");
//        sensor2.addProperty("maxThreshold", "10");
//        sensor2.addProperty("exceptionDuration", "2");
//        sensorRuleItems.add(sensor2);
//
//        // Add more sensors similarly...
//
//        // Attach the array to the root object
//        payload.add("sensorRuleItems", sensorRuleItems);
//
//        // Convert to JSON string
//        String jsonString = payload.toString();
//        System.out.println(jsonString);

//        =============================================================================

        // Create the root object
        JSONObject payload = new JSONObject();
        payload.put("ruleSetName", "newRule0124202501");
        payload.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
        payload.put("emails", "test@example.com");
        payload.put("visibilityLevel", "OrganizationOnly");

        // Create the sensorRuleItems array
        JSONArray sensorRuleItems = new JSONArray();

        // Add items to the array
        JSONObject sensor1 = new JSONObject();
        sensor1.put("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
        sensor1.put("sensorTypeName", "probeTemp");
        sensor1.put("isEnabled", true);
        sensor1.put("minThreshold", "5");
        sensor1.put("maxThreshold", "10");
        sensor1.put("exceptionDuration", "1");
        sensorRuleItems.put(sensor1);

        JSONObject sensor2 = new JSONObject();
        sensor2.put("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
        sensor2.put("sensorTypeName", "deviceTemp");
        sensor2.put("isEnabled", true);
        sensor2.put("minThreshold", "5");
        sensor2.put("maxThreshold", "10");
        sensor2.put("exceptionDuration", "2");
        sensorRuleItems.put(sensor2);

        // Add more sensors similarly...

        // Attach the array to the root object
        payload.put("sensorRuleItems", sensorRuleItems);

        // Convert to JSON string
        String jsonString = payload.toString(4); // Pretty print with 4 spaces
        System.out.println(jsonString);

//        =======================================================================================
        // Create the root map
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("ruleSetName", "newRule0124202501");
//        payload.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
//        payload.put("emails", "test@example.com");
//        payload.put("visibilityLevel", "OrganizationOnly");
//
//        // Create the sensorRuleItems list
//        List<Map<String, Object>> sensorRuleItems = new ArrayList<>();
//
//        // Add items to the list
//        Map<String, Object> sensor1 = new HashMap<>();
//        sensor1.put("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
//        sensor1.put("sensorTypeName", "probeTemp");
//        sensor1.put("isEnabled", true);
//        sensor1.put("minThreshold", "5");
//        sensor1.put("maxThreshold", "10");
//        sensor1.put("exceptionDuration", "1");
//        sensorRuleItems.add(sensor1);
//
//        Map<String, Object> sensor2 = new HashMap<>();
//        sensor2.put("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
//        sensor2.put("sensorTypeName", "deviceTemp");
//        sensor2.put("isEnabled", true);
//        sensor2.put("minThreshold", "5");
//        sensor2.put("maxThreshold", "10");
//        sensor2.put("exceptionDuration", "2");
//        sensorRuleItems.add(sensor2);
//
//        // Add more sensors as needed...
//
//        // Attach the list to the root map
//        payload.put("sensorRuleItems", sensorRuleItems);
//
//        // Print the structure
//        System.out.println(payload);
//
//        ============================================================================================

        // Create the root map
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("ruleSetName", "newRule0124202501");
//        payload.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
//        payload.put("emails", "test@example.com");
//        payload.put("visibilityLevel", "OrganizationOnly");
//
//        // Create the sensorRuleItems array
//        Map<String, Object>[] sensorRuleItems = new Map[2]; // Example size 2
//
//        // Add items to the array
//        Map<String, Object> sensor1 = new HashMap<>();
//        sensor1.put("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
//        sensor1.put("sensorTypeName", "probeTemp");
//        sensor1.put("isEnabled", true);
//        sensor1.put("minThreshold", "5");
//        sensor1.put("maxThreshold", "10");
//        sensor1.put("exceptionDuration", "1");
//        sensorRuleItems[0] = sensor1;
//
//        Map<String, Object> sensor2 = new HashMap<>();
//        sensor2.put("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
//        sensor2.put("sensorTypeName", "deviceTemp");
//        sensor2.put("isEnabled", true);
//        sensor2.put("minThreshold", "5");
//        sensor2.put("maxThreshold", "10");
//        sensor2.put("exceptionDuration", "2");
//        sensorRuleItems[1] = sensor2;
//
//        // Attach the array to the root map
//        payload.put("sensorRuleItems", sensorRuleItems);
//
//        // Print the structure
//        System.out.println(Arrays.toString(sensorRuleItems));
//        System.out.println(payload);
//
//        =====================================================================================

        // Create the root HashMap
//        HashMap<String, Object> payload = new HashMap<>();
//        payload.put("ruleSetName", "newRule0124202501");
//        payload.put("organizationId", "49194360-27ab-4434-991f-88116dc775bb");
//        payload.put("emails", "test@example.com");
//        payload.put("visibilityLevel", "OrganizationOnly");
//
//        // Create the sensorRuleItems list
//        List<HashMap<String, Object>> sensorRuleItems = new ArrayList<>();
//
//        // Add items to the list
//        HashMap<String, Object> sensor1 = new HashMap<>();
//        sensor1.put("sensorTypeId", "cdba0280-3dee-41b5-b684-30e138b62f59");
//        sensor1.put("sensorTypeName", "probeTemp");
//        sensor1.put("isEnabled", true);
//        sensor1.put("minThreshold", "5");
//        sensor1.put("maxThreshold", "10");
//        sensor1.put("exceptionDuration", "1");
//        sensorRuleItems.add(sensor1);
//
//        HashMap<String, Object> sensor2 = new HashMap<>();
//        sensor2.put("sensorTypeId", "5a9cc6e4-02c4-40c8-971e-28d20cfd98a5");
//        sensor2.put("sensorTypeName", "deviceTemp");
//        sensor2.put("isEnabled", true);
//        sensor2.put("minThreshold", "5");
//        sensor2.put("maxThreshold", "10");
//        sensor2.put("exceptionDuration", "2");
//        sensorRuleItems.add(sensor2);
//
//        // Add more sensors as needed...
//
//        // Attach the list to the root map
//        payload.put("sensorRuleItems", sensorRuleItems);
//
//        // Print the structure
//        System.out.println(payload);
    }
}