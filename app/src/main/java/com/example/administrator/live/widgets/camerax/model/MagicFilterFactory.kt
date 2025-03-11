package com.example.administrator.live.widgets.camerax.model

import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.GPUImageFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicAmaroFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicAntiqueFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicBlackCatFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicBrannanFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicBrooklynFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicCalmFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicCoolFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicEarlyBirdFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicEmeraldFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicEvergreenFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicFairytaleFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicFreudFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicHealthyFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicHefeFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicHudsonFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicInkwellFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicKevinFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicLatteFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicN1977Filter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicNashvilleFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicPixarFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicRiseFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicRomanceFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSakuraFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSierraFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSkinWhitenFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSunriseFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSunsetFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSutroFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicSweetsFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicTenderFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicToasterFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicValenciaFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicWaldenFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicWarmFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicWhiteCatFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.baseFilter.MagicXproIIFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageBrightnessFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageContrastFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageExposureFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageHueFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageSaturationFilter
import com.example.administrator.live.widgets.camerax.filters.gpuFilters.paramsFilter.GPUImageSharpenFilter

class MagicFilterFactory {
    companion object {
        var currentFilterType = MagicFilterType.NONE
            private set

        fun initFilters(type: MagicFilterType?): GPUImageFilter? {
            if (type == null) {
                return null
            }
            currentFilterType = type
            return when (type) {
                MagicFilterType.WHITECAT -> MagicWhiteCatFilter()
                MagicFilterType.BLACKCAT -> MagicBlackCatFilter()
                MagicFilterType.SKINWHITEN -> MagicSkinWhitenFilter()
                MagicFilterType.ROMANCE -> MagicRomanceFilter()
                MagicFilterType.SAKURA -> MagicSakuraFilter()
                MagicFilterType.AMARO -> MagicAmaroFilter()
                MagicFilterType.WALDEN -> MagicWaldenFilter()
                MagicFilterType.ANTIQUE -> MagicAntiqueFilter()
                MagicFilterType.CALM -> MagicCalmFilter()
                MagicFilterType.BRANNAN -> MagicBrannanFilter()
                MagicFilterType.BROOKLYN -> MagicBrooklynFilter()
                MagicFilterType.EARLYBIRD -> MagicEarlyBirdFilter()
                MagicFilterType.FREUD -> MagicFreudFilter()
                MagicFilterType.HEFE -> MagicHefeFilter()
                MagicFilterType.HUDSON -> MagicHudsonFilter()
                MagicFilterType.INKWELL -> MagicInkwellFilter()
                MagicFilterType.KEVIN -> MagicKevinFilter()
                MagicFilterType.N1977 -> MagicN1977Filter()
                MagicFilterType.NASHVILLE -> MagicNashvilleFilter()
                MagicFilterType.PIXAR -> MagicPixarFilter()
                MagicFilterType.RISE -> MagicRiseFilter()
                MagicFilterType.SIERRA -> MagicSierraFilter()
                MagicFilterType.SUTRO -> MagicSutroFilter()
                MagicFilterType.TOASTER2 -> MagicToasterFilter()
                MagicFilterType.VALENCIA -> MagicValenciaFilter()
                MagicFilterType.XPROII -> MagicXproIIFilter()
                MagicFilterType.EVERGREEN -> MagicEvergreenFilter()
                MagicFilterType.HEALTHY -> MagicHealthyFilter()
                MagicFilterType.COOL -> MagicCoolFilter()
                MagicFilterType.EMERALD -> MagicEmeraldFilter()
                MagicFilterType.LATTE -> MagicLatteFilter()
                MagicFilterType.WARM -> MagicWarmFilter()
                MagicFilterType.TENDER -> MagicTenderFilter()
                MagicFilterType.SWEETS -> MagicSweetsFilter()
                MagicFilterType.FAIRYTALE -> MagicFairytaleFilter()
                MagicFilterType.SUNRISE -> MagicSunriseFilter()
                MagicFilterType.SUNSET -> MagicSunsetFilter()
                MagicFilterType.BRIGHTNESS -> GPUImageBrightnessFilter()
                MagicFilterType.CONTRAST -> GPUImageContrastFilter()
                MagicFilterType.EXPOSURE -> GPUImageExposureFilter()
                MagicFilterType.HUE -> GPUImageHueFilter()
                MagicFilterType.SATURATION -> GPUImageSaturationFilter()
                MagicFilterType.SHARPEN -> GPUImageSharpenFilter()
                else -> null
            }
        }
    }
}