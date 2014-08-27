/*
 * Java Genetic Algorithm Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jenetics;

import java.util.Arrays;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version <em>$Date: 2014-08-27 $</em>
 */
public class ProbabilitySelectorTest {

    private static double[] array(final int size, final Random random) {
        final double[] array = new double[size];
        for (int i = 0; i < array.length; ++i) {
            array[i] = i;
        }

        shuffle(array, random);
        return array;
    }

    public static void shuffle(final double[] array, final Random random) {
        for (int i = array.length; --i >=0;) {
            swap(array, i, random.nextInt(array.length));
        }
    }

    public static void swap(final double[] array, final int i, final int j) {
        final double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @DataProvider(name = "arraySize")
    public Object[][] arraySize() {
        return new Object[][]{
            {6}, {100}, {1000}, {10_000}, {100_000}, {500_000}
        };
    }

    @Test(dataProvider = "arraySize")
    public void revert(final Integer size) {
        final double[] probabilities = array(size, new Random());
        final double[] reverted = ProbabilitySelector.sortAndRevert(probabilities);

        //System.out.println(Arrays.toString(probabilities));
        //System.out.println(Arrays.toString(reverted));

        for (int i = 0; i < size; ++i) {
            Assert.assertEquals(
                probabilities[i] + reverted[i],
                size - 1.0
            );
        }
    }

    @Test(dataProvider = "arraySize")
    public void revertSortedArray(final Integer size) {
        final double[] values = array(size, new Random());
        Arrays.sort(values);

        final double[] reverted = ProbabilitySelector.sortAndRevert(values);
        for (int i = 0; i < values.length; ++i) {
            Assert.assertEquals(reverted[i], (double)(values.length - i - 1));
        }
    }

//	@Test
//	public void performance() {
//		final Random random = new Random(123);
//		final double[] probabilities = array(200, random);
//
//		final Timer quickSortTimer = new Timer("Quick sort");
//		final Timer insertionSortTimer = new Timer("Insertion sort");
//		for (int i = 0; i < 100000; ++i) {
//			shuffle(probabilities, random);
//
//			quickSortTimer.start();
//			ProbabilitySelector.quickSort(probabilities);
//			quickSortTimer.stop();
//
//			shuffle(probabilities, random);
//
//			insertionSortTimer.start();
//			ProbabilitySelector.insertionSort(probabilities);
//			insertionSortTimer.stop();
//		}
//
//		System.out.println(quickSortTimer);
//		System.out.print(insertionSortTimer);
//	}
//
//	private static <T> void shuffle(final double[] array, final Random random) {
//		for (int j = array.length - 1; j > 0; --j) {
//			swap(array, j, random.nextInt(j + 1));
//		}
//	}
//
//	private static void swap(final double[] indexes, final int i, final int j) {
//		final double temp = indexes[i];
//		indexes[i] = indexes[j];
//		indexes[j] = temp;
//	}

//	private static double[] invert(final double[] probabilities) {
//		final double multiplier = 1.0/(probabilities.length - 1.0);
//		for (int i = 0; i < probabilities.length; ++i) {
//			probabilities[i] = (1.0 - probabilities[i])*multiplier;
//		}
//		return probabilities;
//	}

}
