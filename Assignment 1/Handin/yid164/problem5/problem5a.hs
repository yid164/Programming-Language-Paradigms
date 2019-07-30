-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecutre: CMPT 340

compose3 :: Double -> Double
compose3 x = f(g(h(x)))

f :: Double -> Double
f a = a + 1.0

g :: Double -> Double
g b = b + 1.0

h :: Double -> Double
h c = c +1.0