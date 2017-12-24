package com.omarsilva.morsetorch

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.omarsilva.morsetorch.HardwareFlashlightController.FlashlightState.OFF
import com.omarsilva.morsetorch.HardwareFlashlightController.FlashlightState.ON
import com.omarsilva.morsetorch.`interface`.Flashlight


class HardwareFlashlightController constructor(private val cameraManager: CameraManager) : Flashlight {

    companion object {
        fun create(cameraManager: CameraManager): HardwareFlashlightController = HardwareFlashlightController(cameraManager)
    }

    override fun turnFlashlightOn() {
        changeFlashlightStatus(ON)
    }

    override fun turnFlashlightOff() {
        changeFlashlightStatus(OFF)
    }

    private fun changeFlashlightStatus(state: FlashlightState) {
        cameraManager.setTorchMode(getBackCameraId(), state.value)
    }

    private fun getBackCameraId() : String {

        for (id in cameraManager.cameraIdList) {
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            val hasFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

            if (facing != null
                    && facing == CameraCharacteristics.LENS_FACING_BACK
                    && hasFlash != null
                    && hasFlash) {
                return id
            }
        }

        return ""
    }

    enum class FlashlightState constructor(val value: Boolean) {
        ON(true),
        OFF(false);
    }
}