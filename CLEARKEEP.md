*** Clear Keep ***

** Code Flow **

This project based on riot-android developed by Matrix, so, we need custom some files for this project work.

riot-android need declare some function on Application, so we need extend VectorApp class and change Application in AndroidManifes.xml file to custom Application class.

DaggerApplication extended form Application, but VectorApp class etended from MultiDexApplication, so we need copy code in DaggerApplication class to custom class

** About **
