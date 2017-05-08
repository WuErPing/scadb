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
package org.herry2038.scadb.db.util

import scala.util._
import scala.util.Success

object Version {

  private def tryParse( index : Int, pieces : Array[String] ) : Int = {

    Try {
      pieces(index).toInt
    } match {
      case Success(value) => value
      case Failure(e) => 0
    }

  }

  def apply( version : String ) : Version = {
    val pieces = version.split('.')
    new Version( tryParse(0, pieces), tryParse(1, pieces), tryParse(2, pieces) )
  }

}

case class Version( major : Int, minor : Int, maintenance : Int ) extends Ordered[Version] {
  override def compare( y: Version): Int = {

    if ( this == y ) {
      return 0
    }

    if ( this.major != y.major ) {
      return this.major.compare(y.major)
    }

    if ( this.minor != y.minor ) {
      return this.minor.compare(y.minor)
    }

    this.maintenance.compare(y.maintenance)
  }
}