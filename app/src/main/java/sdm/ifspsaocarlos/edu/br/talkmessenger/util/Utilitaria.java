package sdm.ifspsaocarlos.edu.br.talkmessenger.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class Utilitaria {

    public static int convertDpToPx(Context context, int dp) {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
