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
package org.herry2038.scadb.db.general

import org.herry2038.scadb.db.RowData
import scala.collection.mutable

class ArrayRowData( columnCount : Int, row : Int, val mapping : Map[String, Int] )
  extends RowData
{

  private val columns = new Array[Any](columnCount)

  /**
   *
   * Returns a column value by it's position in the originating query.
   *
   * @param columnNumber
   * @return
   */
  def apply(columnNumber: Int): Any = columns(columnNumber)

  /**
   *
   * Returns a column value by it's name in the originating query.
   *
   * @param columnName
   * @return
   */
  def apply(columnName: String): Any = columns( mapping(columnName) )

  /**
   *
   * Number of this row in the query results. Counts start at 0.
   *
   * @return
   */
  def rowNumber: Int = row

  /**
   *
   * Sets a value to a column in this collection.
   *
   * @param i
   * @param x
   */

  def update(i: Int, x: Any) = columns(i) = x

  def length: Int = columns.length

}
