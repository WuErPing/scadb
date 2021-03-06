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
package org.herry2038.scadb.mysql.message.client

import java.nio.charset.Charset

case class HandshakeResponseMessage(
                                     username: String,
                                     charset: Charset,
                                     seed: Array[Byte],                                     
                                     authenticationMethod: String,
                                     password: Option[String] = None,
                                     database: Option[String] = None
                                     )
  extends ClientMessage(ClientMessage.ClientProtocolVersion) {
  var scramble: String = null
  def this(username: String,charset: Charset, scramble: String, authenticationMethod: String,database: Option[String]) = {
    this(username,charset,null,authenticationMethod,None,database)
    this.scramble = scramble
  } 
}
