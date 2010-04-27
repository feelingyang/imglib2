/**
 * Copyright (c) 2009--2010, Stephan Preibisch & Stephan Saalfeld
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.  Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials
 * provided with the distribution.  Neither the name of the Fiji project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Stephan Preibisch & Stephan Saalfeld
 */
package mpicbg.imglib.cursor;

import mpicbg.imglib.container.Container;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.Type;

/**
 * We use the class {@link AbstractCursor} instead of implementing methods here so that other classes can
 * only implement {@link IterableCursor} and extend other classes instead. As each {@link AbstractCursor} is also
 * a {@link IterableCursor} there are no disadvantages for the {@link IterableCursor} implementations.
 * 
 * @author Stephan Preibishc and Stephan Saalfeld
 *
 * @param <T>
 */
public abstract class AbstractCursor<T extends Type<T>> implements Cursor<T>
{
	final protected Image<T> image;
	final protected Container<T> container;
	protected boolean isClosed = false;
	final protected int numDimensions;
	
	public AbstractCursor( final Container<T> container, final Image<T> image )
	{
		this.image = image;
		this.container = container;
		numDimensions = container.numDimensions();
	}

	@Override
	@Deprecated
	final public T getType(){ return type(); } 
	
	@Override
	public int getArrayIndex() { return type().getIndex(); }
	@Override
	public Image<T> getImage() { return image; }
	@Override
	public boolean isActive() { return !isClosed; }	
}
