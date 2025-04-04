/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.administrator.live.widgets.camerax.filters.gpuFilters.utils

import java.lang.IllegalStateException

enum class Rotation {
    NORMAL, ROTATION_90, ROTATION_180, ROTATION_270;

    /**
     * Retrieves the int representation of the Rotation.
     *
     * @return 0, 90, 180 or 270
     */
    fun asInt(): Int {
        return when (this) {
            NORMAL -> 0
            ROTATION_90 -> 90
            ROTATION_180 -> 180
            ROTATION_270 -> 270
            else -> throw IllegalStateException("Unknown Rotation!")
        }
    }

    companion object {
        /**
         * Create a Rotation from an integer. Needs to be either 0, 90, 180 or 270.
         *
         * @param rotation 0, 90, 180 or 270
         * @return Rotation object
         */
        fun fromInt(rotation: Int): Rotation {
            return when (rotation) {
                0 -> NORMAL
                90 -> ROTATION_90
                180 -> ROTATION_180
                270 -> ROTATION_270
                360 -> NORMAL
                else -> throw IllegalStateException("$rotation is an unknown rotation. Needs to be either 0, 90, 180 or 270!")
            }
        }
    }
}