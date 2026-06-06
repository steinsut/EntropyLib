
EntropyLib
=======

_For when Minecraft modding is just too darn lacking in disorder._

A library mod for another mod that is in development (privately, for now). Because a feature
I was adding required enough effort that it could basically be its own thing and potentially even
be useful for other developers.

Current features:
- The **Dyn** system: Normally, entities and their renderers have a one-to-one relationship, so any entity 
variation had to be handled using render states. This works around that by providing an API to support on the fly
renderer switching for any entity (at least behavior wise), along with custom data storage and synchronization support for ease
of use. Base classes are provided for the different base entity types (LivingEntity, Projectile etc.) as default 
Dyn entity implementations.

## Usage
No artifact uploads right now. (read: [W.I.P](https://dictionary.cambridge.org/dictionary/english/wip)). Current steps are:

1. Clone the Git repository
2. Publish to local Maven repository (Run the ``publishMavenJavaPublicationToMavenLocal`` task)
3. Add the following to your ``build.gradle`` file:
```groovy
repositories {
    //adds the local Maven repository to the project
    mavenLocal()
}

dependencies {
    //actually adds the dependencies
    //compileOnly: Depend on the API classes ONLY for compiling
    //localRuntime: Use the full library implementation for running the client
    compileOnly "me.steinut:entropylib-api:${entropylib_version}"
    localRuntime "me.steinut:entropylib:${entropylib_version}"
}
```
4. ???
5. Happy coding!

## Contributions
Always welcome. Open a PR and I'll take a look (probably, maybe).

# WARNING
This mod is currently very much in alpha. Any breaking changes may occur at any time.

