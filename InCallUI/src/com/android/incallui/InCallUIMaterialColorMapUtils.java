package com.android.incallui;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.telecom.PhoneAccount;

import com.android.contacts.common.util.MaterialColorMapUtils;
import com.android.contacts.common.util.MaterialColorMapUtils.MaterialPalette;
import com.android.dialer.R;
import android.provider.Settings;
import android.graphics.Color;

public class InCallUIMaterialColorMapUtils extends MaterialColorMapUtils {
    private final TypedArray sPrimaryColors;
    private final TypedArray sSecondaryColors;
    private final Resources mResources;

    private Context mContext;

    public InCallUIMaterialColorMapUtils(Context context, Resources resources) {
        super(resources);
        sPrimaryColors = resources.obtainTypedArray(R.array.background_colors);
        sSecondaryColors = resources.obtainTypedArray(R.array.background_colors_dark);
        mResources = resources;
	mContext = context;
    }

    /**
     * Currently the InCallUI color will only vary by SIM color which is a list of colors
     * defined in the background_colors array, so first search the list for the matching color and
     * fall back to the closest matching color if an exact match does not exist.
     */
    @Override
    public MaterialPalette calculatePrimaryAndSecondaryColor(int color) {
//	ContentResolver cr = mContext.getContentResolver();
        if (color == PhoneAccount.NO_HIGHLIGHT_COLOR) {
            return getDefaultPrimaryAndSecondaryColors(mResources);
        }
     if (Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.INCALL_BG_KEY, 0) == 1) {
	      return UserPrimaryAndSecondaryColors();
      }

        for (int i = 0; i < sPrimaryColors.length(); i++) {
            if (sPrimaryColors.getColor(i, 0) == color) {
                return new MaterialPalette(
                        sPrimaryColors.getColor(i, 0),
                        sSecondaryColors.getColor(i, 0));
            }
        }
        // The color isn't in the list, so use the superclass to find an approximate color.
        return super.calculatePrimaryAndSecondaryColor(color);
    }

    /**
     * {@link Resources#getColor(int) used for compatibility
     */
    @SuppressWarnings("deprecation")
    public static MaterialPalette getDefaultPrimaryAndSecondaryColors(Resources resources) {
        final int primaryColor = resources.getColor(R.color.dialer_theme_color);
        final int secondaryColor = resources.getColor(R.color.dialer_theme_color_dark);
        return new MaterialPalette(primaryColor, secondaryColor);
    }

    public MaterialPalette UserPrimaryAndSecondaryColors() {
	final int primaryColor = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.INCALL_BG_PRIMARY, Color.parseColor("#FF444444"));
	final int secondaryColor = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.INCALL_BG_SECONDARY, Color.parseColor("#FF007099"));
        return new MaterialPalette(primaryColor, secondaryColor);
    }
}
