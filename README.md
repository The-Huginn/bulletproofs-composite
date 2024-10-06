# Composite Proofs with Bulletproofs

This project demonstrates how to create composite proofs on top of bulletproofs. Bulletproofs are a cryptographic proof
technique that allows for short, non-interactive zero-knowledge proofs that do not require a trusted setup. Composite
proofs enhance this by combining multiple proofs into a single, efficient proof.

This library extends [bulletproofs-gadgets](https://github.com/weavechain/bulletproofs-gadgets) and allows for creating
a single proof out of multiple gadgets, while still keeping the proof valid. 

## Features

- **Efficient Zero-Knowledge Proofs:** Using bulletproofs to create short and efficient proofs.
- **Composite Proofs:** Combining multiple bulletproofs into a single proof for enhanced verification efficiency and
 smaller digital footprint.
- **No Trusted Setup:** Relies on the strong cryptographic properties of bulletproofs without the need for an initial
  trusted setup.

## Getting Started

### Installing

1.  **You need to recompile slightly updated bulletproofs**
    ```bash
    git clone https://github.com/The-Huginn/bulletproofs.git
    cd bulletproofs
    ./gradlew publishToMavenLocal
    cd ..
    ```

2.  **Clone and build the project:**
    ```bash
    git clone https://github.com/The-Huginn/bulletproofs-composite.git
    cd bulletproofs-composite
    mvn clean install
    ```

## Usage

To generate a composite proof using bulletproofs, you can use the following code snippet:

```kotlin
fun main() {
    val compositeApi = DefaultCompositeApi()
    val compositeProof = compositeApi.createProof(
        DefaultProvingGadget(
            NumberInRangeParams(10, 100, 31),
            16
        )
    )

    val match = compositeApi.verifyProof(
        compositeProof,
        DefaultVerifyingGadget(NumberInRangeParams(10, 100, 31))
    )
    
    println(if (match) "Success" else "Fail")
}
```

You can check `CompositeApiTest` for few more examples.

## Acknowledgements

- **Ioan Moldovan:** Special thanks to the developer for the bulletproofs & gadgets implementation