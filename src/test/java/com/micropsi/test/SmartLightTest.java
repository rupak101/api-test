package com.micropsi.test;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.api.clients.RestAssuredConnectDeviceClient.getClientForConnectDevice;
import static com.api.clients.RestAssuredDeviceBrightnessClient.getClientForDeviceBrightness;
import static com.api.clients.RestAssuredDeviceColorClient.getClientForDeviceColor;
import static com.api.clients.RestAssuredDeviceListClient.getClientForDevices;
import static com.api.clients.RestAssuredDeviceNameClient.getClientForDeviceName;
import static com.api.clients.RestAssuredDeviceStateClient.getClientForDeviceState;
import static com.api.clients.RestAssuredDisconnectDeviceClient.getClientForDisconnectDevice;
import static com.api.framework.utils.getJsonPath;
import static com.api.framework.utils.getJsonPayload;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Owner("Rupak Mansingh")
@Feature("To manage smart light bulbs in home network")
public class SmartLightTest {
    private String response;

    //Thread local json as tests executes in parallel
    private static ThreadLocal<JsonPath> js = new ThreadLocal<>();
    private static final String anotherDeviceIP = "192.168.100.11";
    private static final String color = "#336699";
    private static final String name = "foobar";

    @DataProvider
    public Object[][] userData() {
        return new Object[][]{
                {"192.168.100.10"}
        };
    }

    @Story("Search for all devices with valid user name(GET /devices)")
    @Test(dataProvider = "userData")
    public void getDevicesListSuccessfully(String expectedIp) {
        //Get device lists
        response = getClientForDevices().getDevices();
        js.set(getJsonPath(response));
        assertThat("IP address didn't match with the response", js.get().getString("[0].ip"), is(expectedIp));
        assertThat("Name is null in the response", js.get().getString("[0].name"), is(notNullValue()));
    }

    @Story("connect to a device (POST /connect)")
    @Test(dataProvider = "userData")
    public void connectDeviceSuccessfully(String ip) {
        //First disconnect the device before connecting
        getClientForDisconnectDevice().deviceDisconnect();
        //Connect to a device
        response = getClientForConnectDevice().connectDevice(getJsonPayload("ip", ip));
        assertSuccessStatus(response, true);
    }

    @Story("connect to a device to get it's state i.e brightness, color, name, etc (GET /state)")
    @Test(dependsOnMethods = "connectDeviceSuccessfully")
    public void getDeviceStateSuccessfully() {
        //get device state i.e brightness, color, name
        response = getClientForDeviceState().getDeviceState();
        js.set(getJsonPath(response));
        assertThat("IP address is null in the response", js.get().getString("ip"), is(notNullValue()));
        assertThat("Name is null in the response", js.get().getString("name"), is(notNullValue()));
        assertThat("Name is null in the response", js.get().getString("color"), is(notNullValue()));
        assertThat("Name is null in the response", js.get().getFloat("brightness"), is(notNullValue()));
    }

    @Story("Change the device name (POST /name)")
    @Test(dependsOnMethods = "connectDeviceSuccessfully")
    public void changeDeviceNameSuccessfully() {
        //change device name
        response = getClientForDeviceName().deviceName(getJsonPayload("name", name));
        assertSuccessStatus(response, true);

        //Get state of device to validate the device name
        js.set(getDeviceStateResponse());
        assertThat("Name didn't match with the response", js.get().getString("name"), is(name));
    }

    @Story("Change the device Brightness (POST /brightness)")
    @Test(dependsOnMethods = "connectDeviceSuccessfully")
    public void changeDeviceBrightnessSuccessfully() {
        response = getClientForDeviceBrightness().deviceBrightness(getJsonPayload("brightness", "4"));
        assertSuccessStatus(response, true);

        //Get state of device to validate the device brightness
        js.set(getDeviceStateResponse());
        assertThat("Brightness didn't match with the response", js.get().getFloat("brightness"), is(4.0F));
    }

    @Story("Change the device color (POST /color)")
    @Test(dependsOnMethods = "connectDeviceSuccessfully")
    public void changeDeviceColorSuccessfully() {
        response = getClientForDeviceColor().deviceColor(getJsonPayload("color", color));
        assertSuccessStatus(response, true);

        //Get state of device to validate the device color
        js.set(getDeviceStateResponse());
        assertThat("Brightness didn't match with the response", js.get().getString("color"), is(color));
    }

    @Story("Connect to another device by disconnecting current connection (POST /connect and /disconnect)")
    @Test(dependsOnMethods = "changeDeviceBrightnessSuccessfully")
    public void connectToAnotherDeviceSuccessfully() {
        //Disconnect the device from the network
        response = getClientForDisconnectDevice().disconnectDeviceResponse();
        assertSuccessStatus(response, true);

        //connect the device to the network
        response = getClientForConnectDevice().connectDevice(getJsonPayload("ip", anotherDeviceIP));
        assertSuccessStatus(response, true);

        getClientForDisconnectDevice().disconnectDeviceResponse();
    }

    @Story("Change the device name without connect to a device (POST /name)")
    @Test()
    public void changeDeviceNameWithoutConnectToDevice() {

        response = getClientForDeviceName().deviceName(getJsonPayload("name", "foobar"));
        assertSuccessStatus(response, false);
    }

    @Story("Change the device brightness without connect to a device (POST /brightness)")
    @Test()
    public void changeDeviceBrightnessWithoutConnectToDevice() {
        response = getClientForDeviceBrightness().deviceBrightness(getJsonPayload("brightness", "4"));
        assertSuccessStatus(response, false);
    }

    @Story("Change the device color without connect to a device (POST /color)")
    @Test()
    public void changeDeviceColorWithoutConnectToDevice() {
        response = getClientForDeviceColor().deviceColor(getJsonPayload("color", "#336699"));
        assertSuccessStatus(response, false);
    }

    private void assertSuccessStatus(String response, boolean updateStatus) {
        js.set(getJsonPath(response));
        assertThat("Device state didn't retrieve successfully", js.get().getBoolean("success"), is(updateStatus));
    }

    private JsonPath getDeviceStateResponse() {
        response = getClientForDeviceState().getDeviceState();
        return getJsonPath(response);
    }
}
