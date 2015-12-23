// Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package wwunderbot.models.grid;

import java.awt.*;

/**
 * Cell class
 * <p>
 * Represents one Cell in the playing field. Has some basic methods already
 * implemented.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class Cell implements Cloneable {
  private Point location;
  private CellType state;

  public Cell() {
    this(null, CellType.EMPTY);
  }

  public Cell(final Point location, final CellType type) {
    this.location = location;
    this.state = type;
  }

  @Override
  public Cell clone() {
    Cell cloned;
    try {
      cloned = (Cell) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }

    if (cloned.location != null)
      cloned.setLocation((Point) location.clone());

    return cloned;
  }

  public boolean isShape() {
    return state == CellType.SHAPE;
  }

  public boolean isSolid() {
    return state == CellType.SOLID;
  }

  public boolean isBlock() {
    return state == CellType.BLOCK;
  }

  public boolean isEmpty() {
    return state == CellType.EMPTY;
  }

  public CellType getState() {
    return state;
  }

  public void setState(CellType state) {
    this.state = state;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point point) {
    this.location = point;
  }
}
