/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package com.jackandabhishek.image_ination;

public final class R {
    public static final class attr {
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarButtonStyle=0x7f010001;
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarStyle=0x7f010000;
    }
    public static final class color {
        public static final int black_overlay=0x7f040000;
    }
    public static final class dimen {
        /**  Default screen margins, per the Android Design guidelines. 

         Example customization of dimensions originally defined in res/values/dimens.xml
         (such as screen margins) for screens with more than 820dp of available width. This
         would include 7" and 10" devices in landscape (~960dp and ~1280dp respectively).
    
         */
        public static final int activity_horizontal_margin=0x7f050000;
        public static final int activity_vertical_margin=0x7f050001;
        /** 
         Per the design guidelines, navigation drawers should be between 240dp and 320dp:
         https://developer.android.com/design/patterns/navigation-drawer.html
    
         */
        public static final int navigation_drawer_width=0x7f050002;
    }
    public static final class drawable {
        public static final int drawer_shadow=0x7f020000;
        public static final int ic_drawer=0x7f020001;
        public static final int ic_launcher=0x7f020002;
    }
    public static final class id {
        public static final int ViewColorPickerHelper=0x7f090008;
        public static final int action_example=0x7f09000f;
        public static final int action_settings=0x7f09000e;
        public static final int browse_gallery_button=0x7f090009;
        public static final int browse_gallery_imageview=0x7f09000b;
        public static final int browsephotos_buttons=0x7f090006;
        public static final int camera_button=0x7f09000d;
        public static final int camera_preview=0x7f09000c;
        public static final int container=0x7f090004;
        public static final int drawer_layout=0x7f090003;
        public static final int dummy_button=0x7f090002;
        public static final int edit_image_button=0x7f09000a;
        public static final int fullscreen_content=0x7f090000;
        public static final int fullscreen_content_controls=0x7f090001;
        public static final int horizontalbar=0x7f090007;
        public static final int navigation_drawer=0x7f090005;
    }
    public static final class layout {
        public static final int activity_edit_image=0x7f030000;
        public static final int activity_main=0x7f030001;
        public static final int fragment_browsephotos=0x7f030002;
        public static final int fragment_camera=0x7f030003;
        public static final int fragment_navigation_drawer=0x7f030004;
    }
    public static final class menu {
        public static final int global=0x7f080000;
        public static final int main=0x7f080001;
    }
    public static final class string {
        public static final int action_example=0x7f060006;
        public static final int action_settings=0x7f060007;
        public static final int app_name=0x7f060000;
        public static final int dummy_button=0x7f06000c;
        public static final int dummy_content=0x7f06000d;
        public static final int edit_image=0x7f06000a;
        public static final int navigation_drawer_close=0x7f060005;
        public static final int navigation_drawer_open=0x7f060004;
        public static final int take_photo=0x7f060008;
        public static final int title_activity_edit_image=0x7f06000b;
        public static final int title_section1_takephoto=0x7f060001;
        public static final int title_section2_browsegallery=0x7f060002;
        public static final int title_section3_otherstuff=0x7f060003;
        public static final int under_construction=0x7f060009;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.

    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.

        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.

    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f070000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f070001;
        public static final int ButtonBar=0x7f070003;
        public static final int ButtonBarButton=0x7f070004;
        public static final int FullscreenActionBarStyle=0x7f070005;
        public static final int FullscreenTheme=0x7f070002;
    }
    public static final class styleable {
        /** 
         Declare custom theme attributes that allow changing which styles are
         used for button bars depending on the API level.
         ?android:attr/buttonBarStyle is new as of API 11 so this is
         necessary to support previous API levels.
    
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarButtonStyle com.jackandabhishek.image_ination:metaButtonBarButtonStyle}</code></td><td></td></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarStyle com.jackandabhishek.image_ination:metaButtonBarStyle}</code></td><td></td></tr>
           </table>
           @see #ButtonBarContainerTheme_metaButtonBarButtonStyle
           @see #ButtonBarContainerTheme_metaButtonBarStyle
         */
        public static final int[] ButtonBarContainerTheme = {
            0x7f010000, 0x7f010001
        };
        /**
          <p>This symbol is the offset where the {@link com.jackandabhishek.image_ination.R.attr#metaButtonBarButtonStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name com.jackandabhishek.image_ination:metaButtonBarButtonStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarButtonStyle = 1;
        /**
          <p>This symbol is the offset where the {@link com.jackandabhishek.image_ination.R.attr#metaButtonBarStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name com.jackandabhishek.image_ination:metaButtonBarStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarStyle = 0;
    };
}
