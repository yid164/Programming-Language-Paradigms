-- Name: Yinsheng Dong
-- Student Number: 11148648
-- NSID: yid164
-- Lecture Section: CMPT 340


-- The altMap function
altMap :: (a -> b) -> (a -> b) -> [a] -> [b]
-- If the list is empty
altMap f1 f2 [] = []
-- If the list is not empty
-- If the list has only 1 element, do the function 1
altMap f1 f2 l  = if ((length l)==1) then (f1((head l)) : [])
-- Otherwise, recursively do the function 1 for 1st element, function 2 for the 1st element after the function 1
                  else (f1 (head l) : f2 ((head.tail) l) : altMap f1 f2 ((tail.tail) l))