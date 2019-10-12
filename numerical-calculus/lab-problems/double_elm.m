function result = double_elm(array)
    result = [];
    for i = 1:length(array)
        result = [result array(i) array(i)];
    end
end
