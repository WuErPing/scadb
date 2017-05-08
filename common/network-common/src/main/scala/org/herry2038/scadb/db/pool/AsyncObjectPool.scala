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

package org.herry2038.scadb.db.pool

import org.herry2038.scadb.db.pool.{PoolAlreadyTerminatedException, PoolExhaustedException}

import scala.concurrent.Future

/**
 *
 * Defines the common interface for async object pools. These are pools that do not block clients trying to acquire
 * a resource from it. Different than the usual synchronous pool, you **must** return objects back to it manually
 * since it's impossible for the pool to know when the object is ready to be given back.
 *
 * @tparam T
 */

trait AsyncObjectPool[T] {

  def name: String
  /**
   *
   * Returns an object from the pool to the callee with the returned future. If the pool can not create or enqueue
   * requests it will fill the returned [[scala.concurrent.Future]] with an
   * [[PoolExhaustedException]].
   *
   * @return future that will eventually return a usable pool object.
   */

  def take: Future[T]

  /**
   *
   * Returns an object taken from the pool back to it. This object will become available for another client to use.
   * If the object is invalid or can not be reused for some reason the [[scala.concurrent.Future]] returned will contain
   * the error that prevented this object of being added back to the pool. The object is then discarded from the pool.
   *
   * @param item
   * @return
   */

  def giveBack( item : T ) : Future[AsyncObjectPool[T]]

  /**
   *
   * Closes this pool and future calls to **take** will cause the [[scala.concurrent.Future]] to raise an
   * [[PoolAlreadyTerminatedException]].
   *
   * @return
   */

  def close : Future[AsyncObjectPool[T]]

  /**
   *
   * Retrieve and use an object from the pool for a single computation, returning it when the operation completes.
   *
   * @param f function that uses the object
   * @return f wrapped with take and giveBack
   */

  def use[A](f : T => Future[A])(implicit executionContext : scala.concurrent.ExecutionContext) : Future[A] =
    take.flatMap { item =>
      f(item).andThen { case _ =>
        giveBack(item)
      }
    }

  def testObjects: Unit

  def status: String
}
