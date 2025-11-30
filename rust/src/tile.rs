use crate::arrays::{get_all_rotations_and_flips, print_2d};
use crate::bitmask::Bitmask;
use crate::input::ORIGINAL_TILES;

pub const TILE_COUNT: usize = ORIGINAL_TILES.len();
pub(crate) fn create_tiles() -> [Tile; TILE_COUNT] {
    let mut tiles: [Tile; TILE_COUNT] = [Tile::default(); TILE_COUNT];
    for (i, tile_base) in ORIGINAL_TILES.iter().enumerate() {
        tiles[i] = Tile::new(tile_base, i);
    }
    tiles
}

pub(crate) struct Tile {
    base: &'static [&'static [i32]],
    tile_number: usize,
    all_positions: &'static [PositionedTile],
}

impl Tile {
    pub(crate) fn new(base: &'static [&'static [i32]], tile_number: usize) -> Self {
        println!("Creating Tile  {}", tile_number);
        print_2d(base);
        let all_positions = Self::create_all_positioned_tiles(base, tile_number);
        println!("Number of positions: {}", all_positions.len());

        for (i, positioned) in all_positions.iter().enumerate() {
            if positioned.id != i {
                panic!(
                    "Positioned tile id mismatch. Expected {}, got {}",
                    i, positioned.id
                );
            }
        }

        Tile {
            base,
            tile_number,
            all_positions,
        }
    }

    fn create_all_positioned_tiles(
        base: &'static [&'static [i32]],
        tile_number: usize,
    ) -> &'static [PositionedTile] {
        let mut rotations: Vec<Vec<Vec<i32>>> = get_all_rotations_and_flips(base)
            .iter()
            .map(|r| r.to_vec())
            .collect();
        rotations.sort_by(|a, b| {
            let a_size = a.len() * a[0].len();
            let b_size = b.len() * b[0].len();
            a_size.cmp(&b_size)
        });

        let mut positioned_tiles: Vec<PositionedTile> = Vec::new();
        for rotation in rotations.iter() {
            let positions = Self::get_all_positions(rotation.clone(), tile_number);
            positioned_tiles.extend(positions);
        }

        Box::leak(positioned_tiles.into_boxed_slice())
    }

    fn get_all_positions(tile_base: Vec<Vec<i32>>, tile_number: usize) -> Vec<PositionedTile> {
        let mut positioned_tiles: Vec<PositionedTile> = Vec::new();
        todo!();
        positioned_tiles
    }
}

impl Default for Tile {
    fn default() -> Self {
        Tile {
            base: &[],
            tile_number: 0,
            all_positions: &[],
        }
    }
}

impl Clone for Tile {
    fn clone(&self) -> Self {
        Tile {
            base: self.base,
            tile_number: self.tile_number,
            all_positions: self.all_positions,
        }
    }
}

impl Copy for Tile {}

pub(crate) struct PositionedTile {
    bitmask: Bitmask,
    id: usize,
}

impl Default for PositionedTile {
    fn default() -> Self {
        PositionedTile {
            bitmask: Bitmask::new(),
            id: 0,
        }
    }
}
