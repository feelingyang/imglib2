/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * @author Stephan Preibisch & Stephan Saalfeld
 */
package mpi.imglib.container.cube;

import mpi.imglib.container.basictypecontainer.DoubleContainer;
import mpi.imglib.cursor.Cursor;
import mpi.imglib.type.Type;

public class DoubleCubeElement<T extends Type<T>> extends CubeElement<DoubleCubeElement<T>, DoubleCube<T>, T> implements DoubleContainer<T>
{
	protected double[] data;
	
	public DoubleCubeElement( DoubleCube<T> parent, int cubeId, int[] dim, int[] offset, int entitiesPerPixel)
	{
		super( parent, cubeId, dim, offset, entitiesPerPixel );
		data = new double[ numEntities ];
	}

	@Override
	public void close(){ data = null; }

	@Override
	public double[] getCurrentStorageArray(Cursor<?> c) { return data; }
}
