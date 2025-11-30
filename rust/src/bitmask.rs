use crate::board::BOARD_CELL_COUNT;
use crate::input::BOARD_WIDTH;
use std::fmt::{Display, Formatter};
use std::ops::{BitAnd, BitOr, BitXor};

pub const BITMASK_ARRAY_LENGTH: usize =
    (BOARD_CELL_COUNT as f64 / BITS_IN_PRIMITIVE as f64).ceil() as usize;
const BITS_IN_PRIMITIVE: usize = 128;

pub(crate) struct Bitmask {
    bits: [u128; BITMASK_ARRAY_LENGTH],
}

impl Bitmask {
    pub(crate) fn new() -> Self {
        Bitmask {
            bits: [0; BITMASK_ARRAY_LENGTH],
        }
    }

    fn or(a: &Bitmask, b: &Bitmask, output: &mut Bitmask) {
        match BITMASK_ARRAY_LENGTH {
            1 => {
                output.bits[0] = a.bits[0] | b.bits[0];
                return;
            }
            _ => {
                for i in 0..BITMASK_ARRAY_LENGTH {
                    output.bits[i] = a.bits[i] | b.bits[i];
                }
            }
        }
    }

    fn xor(a: &Bitmask, b: &Bitmask, output: &mut Bitmask) {
        match BITMASK_ARRAY_LENGTH {
            1 => {
                output.bits[0] = a.bits[0] ^ b.bits[0];
                return;
            }
            _ => {
                for i in 0..BITMASK_ARRAY_LENGTH {
                    output.bits[i] = a.bits[i] ^ b.bits[i];
                }
            }
        }
    }

    fn and(a: &Bitmask, b: &Bitmask, output: &mut Bitmask) {
        match BITMASK_ARRAY_LENGTH {
            1 => {
                output.bits[0] = a.bits[0] & b.bits[0];
                return;
            }
            _ => {
                for i in 0..BITMASK_ARRAY_LENGTH {
                    output.bits[i] = a.bits[i] & b.bits[i];
                }
            }
        }
    }

    fn set_bit(&mut self, index: usize) {
        let array_index = index / BITS_IN_PRIMITIVE;
        let bit_index = index % BITS_IN_PRIMITIVE;
        self.bits[array_index] |= 1 << bit_index;
    }

    fn clear_bit(&mut self, index: usize) {
        let array_index = index / BITS_IN_PRIMITIVE;
        let bit_index = index % BITS_IN_PRIMITIVE;
        self.bits[array_index] &= !(1 << bit_index);
    }

    fn and_is_zero(&self, other: &Bitmask) -> bool {
        match BITMASK_ARRAY_LENGTH {
            1 => (self.bits[0] & other.bits[0]) == 0,
            _ => {
                for i in 0..BITMASK_ARRAY_LENGTH {
                    if (self.bits[i] & other.bits[i]) != 0 {
                        return false;
                    }
                }
                true
            }
        }
    }

    fn count_ones(&self) -> u32 {
        let mut count = 0;
        for word in &self.bits {
            count += word.count_ones();
        }
        count
    }

    fn and_xor_count_ones(a: &Bitmask, b: &Bitmask, c: &Bitmask) -> u32 {
        match BITMASK_ARRAY_LENGTH {
            1 => ((a.bits[0] & b.bits[0]) ^ c.bits[0]).count_ones(),
            _ => {
                let mut count = 0;
                for i in 0..BITMASK_ARRAY_LENGTH {
                    count += (a.bits[i] & b.bits[i] ^ c.bits[i]).count_ones();
                }
                count
            }
        }
    }

    fn and_equals(a: &Bitmask, b: &Bitmask, c: &Bitmask) -> bool {
        match BITMASK_ARRAY_LENGTH {
            1 => (a.bits[0] & b.bits[0]) == c.bits[0],
            _ => {
                for i in 0..BITMASK_ARRAY_LENGTH {
                    if (a.bits[i] & b.bits[i]) != c.bits[i] {
                        return false;
                    }
                }
                true
            }
        }
    }
}

impl BitOr for Bitmask {
    type Output = Bitmask;

    fn bitor(self, rhs: Self) -> Self::Output {
        let mut output = Bitmask::new();
        Bitmask::or(&self, &rhs, &mut output);
        output
    }
}

impl BitXor for Bitmask {
    type Output = Bitmask;

    fn bitxor(self, rhs: Self) -> Self::Output {
        let mut output = Bitmask::new();
        Bitmask::xor(&self, &rhs, &mut output);
        output
    }
}

impl BitAnd for Bitmask {
    type Output = Bitmask;

    fn bitand(self, rhs: Self) -> Self::Output {
        let mut output = Bitmask::new();
        Bitmask::and(&self, &rhs, &mut output);
        output
    }
}

impl Default for Bitmask {
    fn default() -> Self {
        Self::new()
    }
}

impl Display for Bitmask {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        let mut array = String::new();
        for word in self.bits {
            array.push_str(format!("{:0BITS_IN_PRIMITIVE$b}", &word).as_str());
        }

        let full = array.split_at(array.len() - BOARD_CELL_COUNT).1.to_string();

        let mut formatted = String::new();
        for c in 0..full.len() {
            formatted.push(full.chars().nth(c).unwrap());
            if (c + 1) % BOARD_WIDTH == 0 && c + 1 != full.len() {
                formatted.push('_');
            }
        }

        write!(f, "{}", formatted)
    }
}

impl From<&Vec<Vec<i32>>> for Bitmask {
    fn from(array: &Vec<Vec<i32>>) -> Self {
        let mut bitmask = Bitmask::new();
        for (i, row) in array.iter().enumerate() {
            for (j, &cell) in row.iter().enumerate() {
                if cell != 0 {
                    let index = i * BOARD_WIDTH + j;
                    bitmask.set_bit(index);
                }
            }
        }
        bitmask
    }
}
