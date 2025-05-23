package com.difrancescogianmarco.arcore_flutter_plugin.flutter_models

class FlutterArCoreHitTestResult(private val distance: Float, private val translation: FloatArray, private val rotation: FloatArray) {
    fun toHashMap(): HashMap<String, Any> {
        val map = HashMap<String, Any>()
        map["distance"] = distance.toDouble()
        map["pose"] = FlutterArCorePose(translation,rotation).toHashMap()
        return map
    }
}