# JavaSourceMapGenerator
a tool for generate source map from a set of Java source file


---
##Build:

```
./gradlew createJar
```
it will build an executable jar which in ``build/libs/JavaSourceMapGenerator-all-1.0-SNAPSHOT.jar``

##Usage:

```
java -jar JavaSourceMapGenerator-all-1.0-SNAPSHOT.jar [input_source_dir] [output_source_map_filename]
```

then the file ``output_source_map_filename`` will contain all Classes qualified name, which may looks like this:
```
android.filterpacks.imageproc.CropRectFilter
android.view.InflateTest.ViewOne
benchmarks.regression.IntConstantMultiplicationBenchmark
android.text.style.ParagraphStyle
android.widget.DialerFilter
...
```