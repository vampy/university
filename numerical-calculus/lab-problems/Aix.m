function result = Aix(nodes, i)
    result = 1;
    for index = 1:length(nodes)
        if index ~= i
            result = result * (nodes(i) - nodes(index));
        end
    end
    result = 1 / result;
end