use bitmask::BITMASK_ARRAY_LENGTH;
use board::BOARD_CELL_COUNT;

mod arrays;
mod bitmask;
mod board;
mod input;
mod tile;

fn main() {
    println!("Board Cell Count: {}", BOARD_CELL_COUNT);
    println!("Bitmask Array Length: {}", BITMASK_ARRAY_LENGTH);
    let environment = Environment::new();
}

struct Environment {
    tiles: [tile::Tile; tile::TILE_COUNT],
}

impl Environment {
    pub fn new() -> Self {
        let tiles = tile::create_tiles();
        Environment { tiles }
    }
}
