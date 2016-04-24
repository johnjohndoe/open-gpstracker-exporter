/*
 * ------------------------------------------------------------------------------
 *  **    Author: René de Groot
 *  ** Copyright: (c) 2016 René de Groot All Rights Reserved.
 *  **------------------------------------------------------------------------------
 *  ** No part of this file may be reproduced
 *  ** or transmitted in any form or by any
 *  ** means, electronic or mechanical, for the
 *  ** purpose, without the express written
 *  ** permission of the copyright holder.
 *  *------------------------------------------------------------------------------
 *  *
 *  *   This file is part of "Open GPS Tracker - Exporter".
 *  *
 *  *   "Open GPS Tracker - Exporter" is free software: you can redistribute it and/or modify
 *  *   it under the terms of the GNU General Public License as published by
 *  *   the Free Software Foundation, either version 3 of the License, or
 *  *   (at your option) any later version.
 *  *
 *  *   "Open GPS Tracker - Exporter" is distributed in the hope that it will be useful,
 *  *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *   GNU General Public License for more details.
 *  *
 *  *   You should have received a copy of the GNU General Public License
 *  *   along with "Open GPS Tracker - Exporter".  If not, see <http://www.gnu.org/licenses/>.
 *  *
 *
 */
package nl.renedegroot.android.opengpstracker.exporter.prepare

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nl.renedegroot.android.opengpstracker.exporter.R
import nl.renedegroot.android.opengpstracker.exporter.databinding.FragmentExportPrepareBinding
import nl.renedegroot.android.opengpstracker.exporter.prepare.PrepareExportModel

/**
 * A placeholder fragment containing a simple view.
 */
class PrepareExportFragment : Fragment() {

    var model = PrepareExportModel()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //var binding = DataBindingUtil.inflate<FragmentExportPrepareBinding>(inflater, R.layout.fragment_export_prepare, container, false)
        var binding: FragmentExportPrepareBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_export_prepare, container, false)
        binding.exportSetting = model

        return binding.root
    }
}