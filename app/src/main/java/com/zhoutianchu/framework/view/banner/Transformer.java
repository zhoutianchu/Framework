package com.zhoutianchu.framework.view.banner;

import android.support.v4.view.ViewPager.PageTransformer;

import com.zhoutianchu.framework.view.banner.transformer.AccordionTransformer;
import com.zhoutianchu.framework.view.banner.transformer.BackgroundToForegroundTransformer;
import com.zhoutianchu.framework.view.banner.transformer.CubeInTransformer;
import com.zhoutianchu.framework.view.banner.transformer.CubeOutTransformer;
import com.zhoutianchu.framework.view.banner.transformer.DefaultTransformer;
import com.zhoutianchu.framework.view.banner.transformer.DepthPageTransformer;
import com.zhoutianchu.framework.view.banner.transformer.FlipHorizontalTransformer;
import com.zhoutianchu.framework.view.banner.transformer.FlipVerticalTransformer;
import com.zhoutianchu.framework.view.banner.transformer.ForegroundToBackgroundTransformer;
import com.zhoutianchu.framework.view.banner.transformer.RotateDownTransformer;
import com.zhoutianchu.framework.view.banner.transformer.RotateUpTransformer;
import com.zhoutianchu.framework.view.banner.transformer.ScaleInOutTransformer;
import com.zhoutianchu.framework.view.banner.transformer.StackTransformer;
import com.zhoutianchu.framework.view.banner.transformer.TabletTransformer;
import com.zhoutianchu.framework.view.banner.transformer.ZoomInTransformer;
import com.zhoutianchu.framework.view.banner.transformer.ZoomOutSlideTransformer;
import com.zhoutianchu.framework.view.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
