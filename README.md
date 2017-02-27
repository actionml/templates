# Templates

These are a set of templates specifically designed for [PIO-Kappa](https://github.com/actionml/pio-kappa).

# API

The generalized API for Templates allows both streaming online-learners and periodic/batch learners, referred to as Kappa and Lambda style respectively.

The API supports:

 - attachment of class methods to PIO-Kappa REST endpoints

No fixed workflow or backing compute or storage engine is enforced though PIO-Kappa provides abstractions for several that may be used.

# Gallery

 - The first prototype Template is the Kappa-style [Contextual Bandit](contextual-bandit-template.md).

# License

[Apache v2](License.txt)
