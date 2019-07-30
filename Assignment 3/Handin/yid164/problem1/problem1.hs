-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecture Section: CMPT 340


-- the Original function of unfold
unfold :: (a -> Bool) -> (a -> b) -> (a -> a) -> a -> [b]
unfold p h t x | p x = []
               | otherwise = h x : unfold p h t (t x)
      
-- the example int2bin function for using unfold    
int2bin :: Int -> [Int]         
int2bin = unfold(==0)(`mod`2)(`div`2)


-- the map function using unfold
map :: (a -> b) -> [a] -> [b]
map f = unfold (null) (f . head) tail


-- the iterate function using unfold
iterate :: (a -> a) -> a -> [a]
iterate f = unfold (const False) id f