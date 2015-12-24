   % 2.4 http://www.learnprolognow.org/lpnpage.php?pagetype=html&pageid=lpn-htmlse7

   word(astante,  a,s,t,a,n,t,e).
   word(astoria,  a,s,t,o,r,i,a).
   word(baratto,  b,a,r,a,t,t,o).
   word(cobalto,  c,o,b,a,l,t,o).
   word(pistola,  p,i,s,t,o,l,a).
   word(statale,  s,t,a,t,a,l,e).

   crossword(V1, V2, V3, H1, H2, H3) :-
       % Vertical
       word(V1, V11, V12, V13, V14, V15, V16, V17),
       word(V2, V21, V22, V23, V24, V25, V26, V27),
       word(V3, V31, V32, V33, V34, V35, V36, V37),

       % Horizontal
       word(H1, H11, V12, H13, V22, H15, V32, H17),
       word(H2, H21, V14, H23, V24, H25, V34, H27),
       word(H3, H31, V16, H33, V26, H35, V36, H37).
