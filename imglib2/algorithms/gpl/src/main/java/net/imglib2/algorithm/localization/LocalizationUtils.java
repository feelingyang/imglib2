/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2013 Stephan Preibisch, Tobias Pietzsch, Barry DeZonia,
 * Stephan Saalfeld, Albert Cardona, Curtis Rueden, Christian Dietz, Jean-Yves
 * Tinevez, Johannes Schindelin, Lee Kamentsky, Larry Lindsey, Grant Harris,
 * Mark Hiner, Aivar Grislis, Martin Horn, Nick Perry, Michael Zinsmaier,
 * Steffen Jaensch, Jan Funke, Mark Longair, and Dimiter Prodanov.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package net.imglib2.algorithm.localization;

import java.util.Random;

import net.imglib2.Cursor;
import net.imglib2.Localizable;
import net.imglib2.algorithm.region.localneighborhood.RectangleCursor;
import net.imglib2.algorithm.region.localneighborhood.RectangleNeighborhoodGPL;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

/**
 * A collection of utility methods for localization algorithms.
 * @author Jean-Yves Tinevez
 */
public class LocalizationUtils {

	private static final EllipticGaussianOrtho g = new EllipticGaussianOrtho();
	private static final Random ran = new Random();
	
	public static final <T extends RealType<T>> void addGaussianSpotToImage(Img<T> img, double[] params) {
		Cursor<T> lc = img.localizingCursor();
		double[] position = new double[img.numDimensions()];
		double val;
		T var = img.firstElement().createVariable();
		while (lc.hasNext()) {
			lc.fwd();
			position[0] = lc.getDoublePosition(0);
			position[1] = lc.getDoublePosition(1);
			val = g.val(position, params);
			var.setReal(val);
			lc.get().add(var);
		}
	}

	public static final <T extends RealType<T>> void addGaussianNoiseToImage(Img<T> img, double sigma_noise) {
		Cursor<T> lc = img.localizingCursor();
		double val;
		T var = img.firstElement().createVariable();
		while (lc.hasNext()) {
			lc.fwd();
			val = Math.max(0, sigma_noise * ran.nextGaussian());
			var.setReal(val);
			lc.get().add(var);
		}
	}
	
	/**
	 * Collects the points to build the observation array, by iterating in a hypercube
	 * around the given location. Points found out of the image are not included.
	 * @param image  the source image to sample.
	 * @param point  the location around which to collect the samples
	 * @param span  the span size of the hypercube to sample, such that in dimension
	 * <code>d</code>, the cube sampled if a of size <code>2 x span[d] + 1</code>.
	 * @return  an {@link Observation} object containing the sampled data.
	 */
	public static final <T extends RealType<T>> Observation gatherObservationData(final Img<T> image, final Localizable point, final long[] span) {
		
		final int ndims = image.numDimensions();
		RectangleNeighborhoodGPL<T, Img<T>> neighborhood = new RectangleNeighborhoodGPL<T, Img<T>>(image);
		neighborhood.setSpan(span);
		neighborhood.setPosition(point);

		int n_pixels = (int) neighborhood.size();
		double[] tmp_I 		= new double[n_pixels];
		double[][] tmp_X 	= new double[n_pixels][ndims];
		
		RectangleCursor<T> cursor = neighborhood.localizingCursor();
		long[] pos = new long[image.numDimensions()];

		int index = 0;
		while (cursor.hasNext()) {
			
			cursor.fwd();
			cursor.localize(pos); // This is the absolute roi position
			if (cursor.isOutOfBounds()) {
				continue;
			}

			for (int i = 0; i < ndims; i++) {
				tmp_X[index][i] = pos[i];
			}
			
			tmp_I[index] = cursor.get().getRealDouble();
			index++;
		} 

		// Now we possibly resize the arrays, in case we have been too close to the 
		// image border.
		double[][] X = null;
		double[] I = null;
		if (index == n_pixels) {
			// Ok, we have gone through the whole square
			X 	= tmp_X;
			I 	= tmp_I;
		} else {
			// Re-dimension the arrays
			X 	= new double[index][ndims];
			I 	= new double[index];
			System.arraycopy(tmp_X, 0, X, 0, index);
			System.arraycopy(tmp_I, 0, I, 0, index);
		}

		Observation obs = new Observation();
		obs.I = I;
		obs.X = X;
		return obs;
	}


}