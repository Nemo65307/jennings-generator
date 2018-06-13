# jennings-generator

Generator consists of two linear-feedback shift registers (LFSR) and a multiplexer. The application generates pseudo-random sequences obtained at the output of the multiplexer. Signals from the parallel outputs of the first m-bit LFSR are fed to information inputs of the multiplexer, and the control inputs are signals from the parallel outputs of the second n-bit LFSR (n<m). Two LFSRs generate sequences with coprime periods. Next state of each LFSR is calculated by multiplying its polynomial's characteristic matrix by the previous state.

 Once the target sequence is generated, there is the option to create an autocorrelation chart.
 
 Polynomials for the application are stored in a remote DB.
 
 More info about the model can be found here - https://en.wikipedia.org/wiki/Linear-feedback_shift_register
