package com.difrancescogianmarco.arcore_flutter_plugin.factory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.Texture
import com.google.ar.sceneform.rendering.ViewRenderable
import kotlinx.coroutines.future.await
import androidx.core.graphics.toColorInt

class CustomShapeFactory(private val context: Context, private val arSceneView: ArSceneView) {
    fun makeText(text: String, color: Int, position: Vector3, rotation: Quaternion) {
        val bitmap = createTextBitmap(text, color)

        Texture.builder()
            .setSource(bitmap)
            .build()
            .thenAccept { texture ->
                MaterialFactory.makeTransparentWithTexture(context, texture)
                    .thenAccept { material ->
                        material.setFloat4("color",
                            com.google.ar.sceneform.rendering.Color(0f, 0f, 0f, 0f)
                        )
                        material.setFloat("metallic", 0f)
                        material.setFloat("roughness", 1f)
                        material.setFloat("reflectance", 0f)
                        material.setBoolean("transparency", true)

                        val renderable = ShapeFactory.makeCube(
                            Vector3(0.4f, 0.2f, 0.0001f),
                            Vector3.zero(),
                            material
                        )

                        renderable.isShadowCaster = false
                        renderable.isShadowReceiver = false

                        Node().apply {
                            this.renderable = renderable
                            localPosition = position
                            localRotation = rotation
                            setParent(arSceneView.scene)
                        }
                    }
            }
            .exceptionally { throwable ->
                Log.e("ARCore", "Failed to create texture or material", throwable)
                null
            }
    }

    private fun createTextBitmap(text: String, color: Int): Bitmap {
        val paint = Paint().apply {
            this.color = color
            textSize = 100f
            isAntiAlias = true
        }
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        val bitmap = createBitmap(bounds.width() + 40, bounds.height() + 40)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvas.drawText(text, 20f, bounds.height().toFloat() + 20f, paint)

        return bitmap
    }
}