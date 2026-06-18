# ExactNumber API 🚀

An ultra-high-performance, deterministic, and immutable hybrid **Fixed-Point Arithmetic Ecosystem** for Java 17+. Specifically engineered for complex game engines and Minecraft modding (Forge/Fabric/NeoForge) where absolute decimal precision is mandatory, and float/double rounding errors cannot be tolerated.

## 🌟 Key Features

* **Zero Floating-Point Drift:** Eliminates classic `0.1 + 0.2 = 0.30000000000000004` errors by processing data using strict base-10 scaled integer mechanics.
* **Dynamic Type Promotion:** Runs natively on primitive 64-bit `long` fields for blistering CPU execution speeds. If an overflow is imminent, it seamlessly morphs into `BigInteger` safety mode, falling back to `long` automatically when values shrink.
* **Thread-Safe & Immutable:** Engineered like Java's `String`. Safe for high-concurrency asynchronous game loops and tick processing.
* **Comprehensive Math Stack:** Out-of-the-box support for Calculus 1, Trigonometry, Linear Algebra, Modular Matrices, Statistics, and Financial structures.


## 📖 Step-by-Step Tutorial: Mastering the Engine

Welcome to the ExactNumber ecosystem! This tutorial is divided into modules to help you understand how to harness the full mathematical power of the API for your game or application.

### 🧱 Module 1: The Core & Factories
`ExactNumber` uses a **Fixed Scale** system. A scale of `6` means there are 6 decimal places (e.g., 10^6). You must always instantiate numbers using our Factories, and **scales must match** when performing operations.
```
// 1. Instantiating from primitive long (Fastest, O(1) complexity)
// Value represents 5.000000
ExactNumber health = ExactNumber.fromLong(5, 6); 

// 2. Instantiating from double (Parsed safely to fixed-point)
// Value represents 15.234000
ExactNumber price = ExactNumber.fromDouble(15.234, 6);

// 3. Global Constants (Singleton - Zero memory overhead)
ExactNumber pi = MathConstants.getInstance().PI;
```


### 🧮 Module 2: Fluent Arithmetic (The Builder Pattern)

Because ExactNumber is immutable, every operation returns a new instance. You can chain operations beautifully like train cars. The library handles the "Hybrid Engine" (promoting to BigInteger if the 64-bit limit is exceeded) automatically under the hood.
```
ExactNumber a = ExactNumber.fromDouble(10.5, 6);
ExactNumber b = ExactNumber.fromDouble(2.0, 6);

// (10.5 + 2.0) * 2.0 - 2.0 = 23.0
ExactNumber result = a.add(b)
                      .multiply(b)
                      .subtract(b);

// Remainder (Modulo) operation is also supported
ExactNumber remainder = a.remainder(b); // 0.500000

System.out.println("Result: " + result.toFormattedString());
```

### 🧩 Module 3: The Microkernel (Using Plugins)

To keep the core class lightweight, advanced math uses the MathPlugin<R> interface. You "inject" a plugin into the number using the apply() method. This is highly useful for Trigonometry and Algebra.
```
import com.engine.math.trigonometry.*;

// 1. Trigonometry (Calculated via Taylor Series for ultimate precision)
ExactNumber angleInRadians = MathConstants.getInstance().PI.divide(ExactNumber.fromLong(2, 6)); // 90 degrees

// Instantiate the plugin with desired precision terms (e.g., 10)
Sine sineAlgorithm = new Sine(10);
ExactNumber sinValue = angleInRadians.apply(sineAlgorithm); // Returns 1.000000

// 2. Polynomial Evaluation (Using Horner's Method for O(N) performance)
// f(x) = 1 + 2x + 3x^2
Polynomial projectileCurve = new Polynomial(
    ExactNumber.fromLong(1, 6), // a0
    ExactNumber.fromLong(2, 6), // a1
    ExactNumber.fromLong(3, 6)  // a2
);

ExactNumber timeX = ExactNumber.fromDouble(2.0, 6);
ExactNumber currentHeight = timeX.apply(projectileCurve); // Evaluates f(2)
```

### 📐 Module 4: Calculus 1

Need to calculate instant velocity (Derivatives) or accumulate area/distance (Integrals)? The API provides numerical solvers.
```
import com.engine.math.calculus.*;

// Using the polynomial curve defined in Module 3
ExactNumber stepH = ExactNumber.fromDouble(0.001, 6); // Approximation step
ExactNumber timeX = ExactNumber.fromDouble(2.0, 6);

// 1. Numerical Derivative (Instant Velocity)
NumericalDerivative derivative = new NumericalDerivative(projectileCurve, stepH);
ExactNumber velocity = timeX.apply(derivative);

// 2. Definite Integral (Total Distance/Area using Trapezoidal Rule)
ExactNumber startA = ExactNumber.fromLong(0, 6);
int slices = 1000; // Precision
NumericalIntegral integral = new NumericalIntegral(projectileCurve, startA, slices);
ExactNumber totalDistance = timeX.apply(integral); // Area from 0 to 2.0
```

### 📊 Module 5: Linear Algebra (Matrices)

Perfect for crafting systems, 3D transformations, or complex fluid dynamics. Matrices are deeply immutable and perform safe defensive copying.
```
import com.engine.math.algebra.linear.Matrix;

ExactNumber one = ExactNumber.fromLong(1, 6);
ExactNumber zero = ExactNumber.fromLong(0, 6);

// Create a 2x2 Matrix
ExactNumber[][] rawData = {
    {one, zero},
    {zero, one}
};
Matrix identityMatrix = new Matrix(rawData);

// Operations strictly validate dimensions to prevent runtime errors (Fail-Fast)
Matrix resultMatrix = identityMatrix.multiply(identityMatrix);
```

### 💰 Module 6: Server Economy & Number Theory

Floating-point errors are the #1 cause of duping exploits in game economies. Use EconomyMath for strict financial calculations and NumberTheory for game progression algorithms.
```
import com.engine.math.financial.EconomyMath;
import com.engine.math.theory.NumberTheory;

// 1. Compound Interest (e.g., Bank systems)
ExactNumber playerBalance = ExactNumber.fromDouble(1000.0, 2); // Scale 2 for coins
ExactNumber dailyRate = ExactNumber.fromDouble(0.05, 2); // 5% interest
int daysOffline = 30;

// O(log N) fast exponentiation prevents server lag
ExactNumber newBalance = EconomyMath.compoundInterest(playerBalance, dailyRate, daysOffline);

// 2. Fibonacci (e.g., XP requirements scaling)
// Uses O(log N) Matrix Exponentiation, NOT slow recursion!
ExactNumber level100Xp = NumberTheory.fibonacci(100, 0);
```

### 📈 Performance Considerations

The system prioritizes bitwise and primitive CPU operations. The BigInteger promotion acts strictly as an asynchronous safety net. Memory allocation (new object initialization) is heavily optimized via instance pooling of recurring game values like ExactNumber.ZERO and ExactNumber.ONE.

Avoid scale mismatches: Always instantiate your numbers with the same scale dimension.

Cache your Plugins: If you need to calculate the Sine of 10,000 entities, instantiate new Sine(10) ONCE and pass it to the .apply() method of all entities to save RAM.

### 📄 License

This project is licensed under the MIT License - feel free to use it in any commercial or open-source Minecraft mod!
