//=========================================================================\\
//     _____               _ _
//    / ____|             | | |
//   | (___   ___ __ _  __| | |__
//    \___ \ / __/ _` |/ _` | '_ \
//    ____) | (_| (_| | (_| | |_) |
//   |_____/ \___\__,_|\__,_|_.__/

// Copyright 2016 The Scadb Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"): you may
// not use this file except in compliance with the License. You may obtain
// a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations
// under the License.
//=========================================================================\\
package org.herry2038.scadb.conf.jobs

import ScadbConfJobsListener.{ScadbConfJobResultListener, ScadbConfJobTaskListener}
import org.herry2038.scadb.conf.ScadbConf

/**
 * Created by Administrator on 2016/3/21.
 */
object JobsManager {
  def startJobsListener(listener: JobsListener) = ScadbConf.pathCache(s"${ScadbConf.basePath}/jobs", new ScadbConfJobTaskListener(listener))

  def startJobResultsListener(listener: JobResultsListener)  = ScadbConf.pathCache(s"${ScadbConf.basePath}/finishedjobs", new ScadbConfJobResultListener(listener))


}