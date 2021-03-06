/*------------------------------------------------------------------------------
 **     Ident: Sogeti Smart Mobile Solutions
 **    Author: René de Groot
 ** Copyright: (c) 2016 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced
 ** Distributed Software Engineering |  or transmitted in any form or by any
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the
 ** 4131 NJ Vianen                   |  purpose, without the express written
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 *
 *   This file is part of OpenGPSTracker.
 *
 *   OpenGPSTracker is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   OpenGPSTracker is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenGPSTracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package nl.sogeti.android.gpstracker.integration;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.HashSet;
import java.util.Set;

/**
 * Asks for Open GPS tracker permissions, forward onRequestPermissionsResult from your Activity to this class
 */
public class PermissionRequestor implements DialogInterface.OnClickListener {

    private static final int REQUEST_CODE = 10000001;
    private static final String INSTALL_URI = "https://play.google.com/store/apps/details?id=nl.sogeti.android.gpstracker";

    private AlertDialog permissionDialog;
    private AlertDialog installDialog;
    private Activity activity;
    private Runnable runnable;
    private Set<String> requestedPermissions = new HashSet<>();

    public void checkTracksPermission(final Activity _activity, Runnable _runnable) {
        checkPermission(_activity, _runnable, R.string.permission_explain_need_content, ServiceConstants.permission.TRACKING_HISTORY);
    }

    public void checkTrackingPermission(final Activity _activity, Runnable _runnable) {
        checkPermission(_activity, _runnable, R.string.permission_explain_need_control, ServiceConstants.permission.TRACKING_CONTROL);
    }

    private void checkPermission(final Activity _activity, Runnable _runnable, @StringRes int message, final String permission) {
        this.activity = _activity;
        this.runnable = _runnable;
        requestedPermissions.add(permission);
        if (ServiceManager.isPackageInstalled(activity)) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionDialog = new AlertDialog.Builder(activity)
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, this)
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                } else {
                    executePermissionsRequest();
                }
            } else {
                runnable.run();
            }
        } else {
            installDialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.permission_missing_title)
                    .setMessage(R.string.permission_missing_message)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(R.string.permission_button_install, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(INSTALL_URI));
                            activity.startActivity(intent);
                        }
                    })
                    .show();
        }
    }

    private void executePermissionsRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(requestedPermissions.toArray(new String[requestedPermissions.size()]), REQUEST_CODE);
        } else {
            runnable.run();
        }
    }

    /**
     * Remove dialogs and context references
     */
    public void stop() {
        if (installDialog != null) {
            installDialog.dismiss();
            installDialog = null;
        }
        if (permissionDialog != null) {
            permissionDialog.dismiss();
            permissionDialog = null;
        }
        runnable = null;
        activity = null;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        executePermissionsRequest();
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            boolean granted = false;
            for (int i = 0; i < permissions.length; i++) {
                if (requestedPermissions.contains(permissions[i])) {
                    granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    if (!granted) {
                        break;
                    }
                }
            }
            if (granted) {
                requestedPermissions.clear();
                runnable.run();
            }
        }
    }
}
