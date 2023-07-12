package com.tech.nyax.farmers_information_management;

import java.util.List;

public interface ScanningAdapter {
    void startScanning();

    void startScanning(String[] uuids);

    void stopScanning();

    List<BTDevice> getFoundDeviceList();
}