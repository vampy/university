# Computer Architecture
[Website Course](http://www.cs.ubbcluj.ro/~vancea/). [Website Lab](http://www.cs.ubbcluj.ro/~radu.dragos/teaching/arhitectura-sistemelor-de-calcul/).

The source code of all the labs.

To run this code you must use [DOSBox](http://www.dosbox.com/). An example configuration file for DOSBox file is in [`dosbox-0.74.conf`](https://github.com/leyyin/university/blob/master/computer-architecture/dosbox-0.74.conf).
On linux you can put the config file in `~/.dosbox/`.

## Compiling
For compiling the 8086 assembly you must use [tasm](https://en.wikipedia.org/wiki/Turbo_Assembler) assembler.
You can download the the compiler, linker, editor, from [here](http://www.cs.ubbcluj.ro/~radu.dragos/asc/tasm-tlink-td.zip). Unarchive the files under a `tasm` directory to the root of your DOSBox directory. The provided config file mounts the `C:` drive from the `~/DOSBox` (see last lines in the provided config file).

In this example we will be compiling a file called `source.asm`.

1. Generate object file (will generate `source.obj` if there are no errors):
`tasm /zi source.asm`

2. Link object file and generate executable:
`tlink /v source.obj`

3. Run file or run (debug) with editor: `td source.exe`

## Documentation
See the [Assembly language Norton Guide](http://www.ousob.com/ng/asm/).

Other docs and courses notes can be found in the `docs` directory.
