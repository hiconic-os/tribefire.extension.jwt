// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.extension.jwt;

import java.util.function.Supplier;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.gm.model.reason.Reasons;
import com.braintribe.gm.model.reason.essential.InternalError;

import tribefire.extension.jwt.processing.PeriodicInitialized;

public class PeriodicInitializedTest {

	@Test
	public void testPeriodicInitialized() {
		NumberSupplier numberSupplier = new NumberSupplier();
		PeriodicInitialized<Integer> holder = new PeriodicInitialized<>(() -> Maybe.complete(numberSupplier.get()));
		holder.setUpdateIntervalInMs(10);

		// calling stuff once to initiate classloading to have proper timing afterwards
		Assertions.assertThat(holder.get().get()).isEqualTo(0);
		numberSupplier.reset();
		holder.reset();

		Assertions.assertThat(holder.get().get()).isEqualTo(0);
		Assertions.assertThat(holder.get().get()).isEqualTo(0);
		Assertions.assertThat(holder.get().get()).isEqualTo(0);

		sleep(11);

		Assertions.assertThat(holder.get().get()).isEqualTo(1);
		Assertions.assertThat(holder.get().get()).isEqualTo(1);
		Assertions.assertThat(holder.get().get()).isEqualTo(1);

		sleep(11);

		Assertions.assertThat(holder.get().get()).isEqualTo(2);
		Assertions.assertThat(holder.get().get()).isEqualTo(2);
		Assertions.assertThat(holder.get().get()).isEqualTo(2);
	}

	@Test
	public void testPeriodicInitializedReasoning() {
		FailingSupplier numberSupplier = new FailingSupplier();
		PeriodicInitialized<Integer> holder = new PeriodicInitialized<>(numberSupplier);
		holder.setUpdateIntervalInMs(10_000);

		Assertions.assertThat(holder.get().whyUnsatisfied().getText()).isEqualTo("0");
		Assertions.assertThat(holder.get().whyUnsatisfied().getText()).isEqualTo("1");
		Assertions.assertThat(holder.get().whyUnsatisfied().getText()).isEqualTo("2");
	}

	private void sleep(long ms) {
		long s = System.nanoTime() / 1_000_000;

		while (true) {
			long c = System.nanoTime() / 1_000_000;

			if (c - s >= ms)
				return;
		}
	}

	class NumberSupplier implements Supplier<Integer> {
		private int number = 0;

		@Override
		public Integer get() {
			return number++;
		}

		public void reset() {
			number = 0;
		}
	}

	class FailingSupplier implements Supplier<Maybe<Integer>> {
		private int number = 0;

		@Override
		public Maybe<Integer> get() {
			return Reasons.build(InternalError.T).text(String.valueOf(number++)).toMaybe();
		}
	}
}
