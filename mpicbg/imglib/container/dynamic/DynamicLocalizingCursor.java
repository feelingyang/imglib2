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
 */
package mpicbg.imglib.container.dynamic;

import java.util.ArrayList;

import mpicbg.imglib.container.AbstractImgLocalizingCursor;
import mpicbg.imglib.image.Image;
import mpicbg.imglib.type.Type;

/**
 * 
 * @param <T>
 *
 * @author Stephan Preibisch and Stephan Saalfeld
 */
final public class DynamicLocalizingCursor< T extends Type< T > > extends AbstractImgLocalizingCursor< T >
{
	private int i;
	final private ArrayList< T > pixels;
	final private DynamicContainer< T > container;
	
	public DynamicLocalizingCursor(	final DynamicContainer< T > container )
	{
		super( container );
		
		this.container = container;
		this.pixels = container.pixels;
		
		reset();
	}
	
	@Override
	public void fwd()
	{ 
		++i; 
		accessor.updateIndex( i );
		
		for ( int d = 0; d < n; d++ )
		{
			if ( position[ d ] < size[ d ] - 1 )
			{
				position[ d ]++;
				
				for ( int e = 0; e < d; e++ )
					position[ e ] = 0;
				
				break;
			}
		}
	}

	@Override
	public boolean hasNext() { return i < container.numPixels() - 1; }
	

	@Override
	public void reset()
	{
		if ( size != null )
		{
			type.updateIndex( 0 );
			i = 0;
			type.updateContainer( this );
			accessor.updateIndex( i );
			i = -1;
			
			position[ 0 ] = -1;
			
			for ( int d = 1; d < n; d++ )
				position[ d ] = 0;		
		}
	}

	@Override
	public DynamicContainer< T, ? > getImg(){ return container; }
	
	@Override
	public T get() { return type; }

	@Override
	public DynamicContainerAccessor getAccessor() { return accessor; }

	@Override
	public int getInternalIndex() { return i; }
}
