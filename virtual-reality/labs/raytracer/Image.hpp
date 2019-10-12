#include "Color.hpp"

#include <cstring>
#include <iostream>
#include <fstream>
#include <string>
#include <cmath>
#include <cstdint>
#include <memory>

#ifndef RT_IMAGE_INCLUDED
#define RT_IMAGE_INCLUDED

namespace rt
{

template <typename T>
T clamp(const T& n, const T& lower, const T& upper) {
  return std::max(lower, std::min(n, upper));
}

/**
 * @brief The Image class
 * @link See https://en.wikipedia.org/wiki/Netpbm_format#PPM_example
 */
class Image
{
private:
    uint16_t m_width;
    uint16_t m_height;
    std::unique_ptr<uint8_t[]> m_buffer;
    size_t m_buffer_length;

public:
    Image(uint16_t width, uint16_t height)
    {
        m_width = width;
        m_height = height;
        m_buffer_length = 3 * m_width * m_height;

        // Create buffer and fill it with 0
        m_buffer = std::unique_ptr<uint8_t[]>(new uint8_t[m_buffer_length]);
        std::memset(m_buffer.get(), 0, m_buffer_length);
    }

    void setPixel(uint16_t x, uint16_t y, const Color& c)
    {
        // Skip y rows + x (current row) times 3 (number of colors)
        size_t k = 3 * (y * m_width + x);

        // Be carefull here not to define an int on 8 bits, we want to clamp ourselfs!!!
        uint16_t p, highest_value = c.isNormalized() ? 255 : 1;

        p = ceil(highest_value * c.getRed());
        if (p > highest_value) p = highest_value;
        if (p < 0) p = 0;
        m_buffer[k] = p;

        p = ceil(highest_value * c.getGreen());
        if (p > highest_value) p = highest_value;
        if (p < 0) p = 0;
        m_buffer[k + 1] = p;

        p = ceil(highest_value * c.getBlue());
        if (p > highest_value) p = highest_value;
        if (p < 0) p = 0;
        m_buffer[k + 2] = p;
    }

    void store(std::string filename)
    {
        std::ofstream out(filename, std::ios::out | std::ios::binary);

        out << "P6" << std::endl;
        out << m_width << " " << m_height << std::endl;
        out << 255 << std::endl;

        out.write((char*) m_buffer.get(), m_buffer_length);
        out.close();
    }
};
}

#endif
