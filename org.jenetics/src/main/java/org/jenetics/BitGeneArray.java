/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 *
 */
package org.jenetics;

import org.jenetics.internal.util.ArrayProxy;
import org.jenetics.internal.util.ArrayProxyISeq;
import org.jenetics.internal.util.ArrayProxyMSeq;

import org.jenetics.util.bit;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @since @__new_version__@
 * @version @__new_version__@ &mdash; <em>$Date: 2013-07-08 $</em>
 */
final class BitGeneArray extends ArrayProxyMSeq<BitGene> {

	public BitGeneArray(final byte[] array, final int start, final int end) {
		super(new Proxy(array, start, end));
	}

	public BitGeneArray(final int length) {
		super(new Proxy(length));
	}

	@Override
	public BitGeneISeq toISeq() {
		return new BitGeneISeq((Proxy)_proxy.seal());
	}

	static final class BitGeneISeq extends ArrayProxyISeq<BitGene> {
		public BitGeneISeq(final Proxy proxy) {
			super(proxy);
		}

		void copyTo(final byte[] array) {
			final Proxy proxy = (Proxy)_proxy;
			System.arraycopy(proxy._array, 0, array, 0, proxy._array.length);
		}

	}

	/*private[jenetics]*/ static final class Proxy extends ArrayProxy<BitGene> {
		private byte[] _array;
		private boolean _sealed = false;

		protected Proxy(final byte[] array, final int start, final int end) {
			super(start, end);
			_array = array;
		}

		Proxy(final int length) {
			this(
				new byte[(length & 7) == 0 ? (length >>> 3) : (length >>> 3) + 1],
				0, length
			);
		}

		@Override
		public BitGene uncheckedOffsetGet(final int absoluteIndex) {
			return BitGene.valueOf(bit.get(_array, absoluteIndex));
		}

		@Override
		public void uncheckedOffsetSet(final int absoluteIndex, final BitGene value) {
			bit.set(_array, absoluteIndex, value.booleanValue());
		}

		@Override
		public Proxy sub(final int start, final int end) {
			return new Proxy(_array, start + _start, end + _start);
		}

		@Override
		public void swap(
			final int start, final int end,
			final ArrayProxy<BitGene> other, final int otherStart
		) {
			cloneIfSealed();
			other.cloneIfSealed();

			if (other instanceof Proxy) {
				swap(start, end, (Proxy)other, otherStart);
			} else {
				for (int i = (end - start); --i >= 0;) {
					final BitGene temp = uncheckedGet(i + start);
					uncheckedSet(i + start, other.uncheckedGet(otherStart + i));
					other.uncheckedSet(otherStart + i, temp);
				}
			}
		}

		private void swap(
			final int start, final int end,
			final Proxy other, final int otherStart
		) {
			checkIndex(start, end);
			other.checkIndex(otherStart, otherStart + (end - start));
			cloneIfSealed();
			other.cloneIfSealed();

			bit.swap(
				_array, start + _start, end + _start,
				other._array, otherStart + other._start
			);

//			for (int i = (end - start); --i >= 0;) {
//				final boolean temp = bit.get(_array, i + start + _start);
//				bit.set(
//					_array, i + start + _start,
//					bit.get(other._array, i + otherStart + other._start)
//				);
//				bit.set(
//					other._array, i + otherStart + other._start,
//					temp
//				);
//			}
		}

		@Override
		public void cloneIfSealed() {
			if (_sealed) {
				_array = _array.clone();
				_sealed = false;
			}
		}

		@Override
		public Proxy seal() {
			_sealed = true;
			return new Proxy(_array, _start, _end);
		}

		@Override
		public Proxy copy() {
			final Proxy proxy = new Proxy(_length);
			System.arraycopy(_array, _start, proxy._array, 0, _length);
			return proxy;
		}

	}

}
