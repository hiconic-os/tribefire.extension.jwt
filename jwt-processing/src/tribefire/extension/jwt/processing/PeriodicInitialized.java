// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package tribefire.extension.jwt.processing;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.braintribe.cfg.Configurable;
import com.braintribe.gm.model.reason.Maybe;
import com.braintribe.utils.lcd.LazyInitialized;

public class PeriodicInitialized<T> extends LazyInitialized<Maybe<T>> {

	private long lastSupply = -1;
	private long updateIntervalInMs = 10 * 60_000;

	public PeriodicInitialized(Supplier<Maybe<T>> constructor, Consumer<Maybe<T>> destructor) {
		super(constructor, destructor);
	}

	public PeriodicInitialized(Supplier<Maybe<T>> constructor) {
		super(constructor);
	}

	@Configurable
	public void setUpdateIntervalInMs(long updateInterval) {
		this.updateIntervalInMs = updateInterval;
	}

	public void reset() {
		lastSupply = -1;
		close();
	}

	@Override
	public Maybe<T> get() {
		long curTime = System.nanoTime() / 1_000_000;

		if (lastSupply == -1 || curTime - lastSupply > updateIntervalInMs) {
			synchronized (this) {
				if (lastSupply == -1 || curTime - lastSupply > updateIntervalInMs) {
					close();
					Maybe<T> maybe = super.get();
					if (maybe.isUnsatisfied())
						close();
					else
						lastSupply = curTime;

					return maybe;
				}
			}
		}

		return super.get();
	}

}
