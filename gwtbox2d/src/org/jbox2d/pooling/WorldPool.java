/*******************************************************************************
 * Copyright (c) 2011, Daniel Murphy All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 * * Neither the name of the <organization> nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL DANIEL MURPHY BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/**
 * Created at 3:26:14 AM Jan 11, 2011
 */
package org.jbox2d.pooling;

import java.util.HashMap;

import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.Distance;
import org.jbox2d.collision.TimeOfImpact;
import org.jbox2d.common.Mat22;
import org.jbox2d.common.Vec2;
import org.jbox2d.common.Vec3;
import org.jbox2d.dynamics.contacts.CircleContact;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.PolygonAndCircleContact;
import org.jbox2d.dynamics.contacts.PolygonContact;

/**
 * Provides object pooling for all objects used in the engine. Objects retrieved from here should
 * only be used temporarily, and then pushed back (with the exception of arrays).
 * 
 * @author Daniel Murphy
 */
public class WorldPool implements IWorldPool {
  private final OrderedStackVec2 vecs;

  private final OrderedStackVec3 vec3s;
  private final OrderedStackMat22 mats;
  private final OrderedStackAABB aabbs;

  private final HashMap<Integer, float[]> afloats = new HashMap<Integer, float[]>();
  private final HashMap<Integer, int[]> aints = new HashMap<Integer, int[]>();
  private final HashMap<Integer, Vec2[]> avecs = new HashMap<Integer, Vec2[]>();

  private final MutableStack<Contact, PolygonContact> pcstack = new PolygonContactStack(this);

  private final MutableStack<Contact, CircleContact> ccstack = new CircleContactStack(this);

  private final MutableStack<Contact, PolygonAndCircleContact> cpstack = new PolygonAndCircleContactStack(this);

  private final Collision collision;
  private final TimeOfImpact toi;
  private final Distance dist;

  public WorldPool(int argSize, int argContainerSize) {
    vecs = new OrderedStackVec2(argSize, argContainerSize);
    vec3s = new OrderedStackVec3(argSize, argContainerSize);
    mats = new OrderedStackMat22(argSize, argContainerSize);
    aabbs = new OrderedStackAABB(argSize, argContainerSize);

    dist = new Distance();
    collision = new Collision(this);
    toi = new TimeOfImpact(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getPolyContactStack()
   */
  @Override
  public final IDynamicStack<Contact> getPolyContactStack() {
    return pcstack;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getCircleContactStack()
   */
  @Override
  public final IDynamicStack<Contact> getCircleContactStack() {
    return ccstack;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getPolyCircleContactStack()
   */
  @Override
  public final IDynamicStack<Contact> getPolyCircleContactStack() {
    return cpstack;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getVec2Stack()
   */
  @Override
  public final IOrderedStack<Vec2> getVec2Stack() {
    return vecs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popVec2()
   */
  @Override
  public final Vec2 popVec2() {
    return vecs.pop();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popVec2(int)
   */
  @Override
  public final Vec2[] popVec2(int argNum) {
    return vecs.pop(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#pushVec2(int)
   */
  @Override
  public final void pushVec2(int argNum) {
    vecs.push(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getVec3Stack()
   */
  @Override
  public final IOrderedStack<Vec3> getVec3Stack() {
    return vec3s;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popVec3()
   */
  @Override
  public final Vec3 popVec3() {
    return vec3s.pop();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popVec3(int)
   */
  @Override
  public final Vec3[] popVec3(int argNum) {
    return vec3s.pop(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#pushVec3(int)
   */
  @Override
  public final void pushVec3(int argNum) {
    vec3s.push(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getMat22Stack()
   */
  @Override
  public final IOrderedStack<Mat22> getMat22Stack() {
    return mats;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popMat22()
   */
  @Override
  public final Mat22 popMat22() {
    return mats.pop();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popMat22(int)
   */
  @Override
  public final Mat22[] popMat22(int argNum) {
    return mats.pop(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#pushMat22(int)
   */
  @Override
  public final void pushMat22(int argNum) {
    mats.push(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getAABBStack()
   */
  @Override
  public final IOrderedStack<AABB> getAABBStack() {
    return aabbs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popAABB()
   */
  @Override
  public final AABB popAABB() {
    return aabbs.pop();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#popAABB(int)
   */
  @Override
  public final AABB[] popAABB(int argNum) {
    return aabbs.pop(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#pushAABB(int)
   */
  @Override
  public final void pushAABB(int argNum) {
    aabbs.push(argNum);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getCollision()
   */
  @Override
  public final Collision getCollision() {
    return collision;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getTimeOfImpact()
   */
  @Override
  public final TimeOfImpact getTimeOfImpact() {
    return toi;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getDistance()
   */
  @Override
  public final Distance getDistance() {
    return dist;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getFloatArray(int)
   */
  @Override
  public final float[] getFloatArray(int argLength) {
    if (!afloats.containsKey(argLength)) {
      afloats.put(argLength, new float[argLength]);
    }

    assert (afloats.get(argLength).length == argLength) : "Array not built with correct length";
    return afloats.get(argLength);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getIntArray(int)
   */
  @Override
  public final int[] getIntArray(int argLength) {
    if (!aints.containsKey(argLength)) {
      aints.put(argLength, new int[argLength]);
    }

    assert (aints.get(argLength).length == argLength) : "Array not built with correct length";
    return aints.get(argLength);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jbox2d.pooling.IWorldPool#getVec2Array(int)
   */
  @Override
  public final Vec2[] getVec2Array(int argLength) {
    if (!avecs.containsKey(argLength)) {
      Vec2[] ray = new Vec2[argLength];
      for (int i = 0; i < argLength; i++) {
        ray[i] = new Vec2();
      }
      avecs.put(argLength, ray);
    }

    assert (avecs.get(argLength).length == argLength) : "Array not built with correct length";
    return avecs.get(argLength);
  }
}
