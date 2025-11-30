use std::collections::HashSet;

pub(crate) fn print_2d(array: &[&[i32]]) {
    for row in array.iter() {
        for &cell in row.iter() {
            print!("{:3} ", cell);
        }
        println!();
    }
}

fn to_owned_matrix(array: &[&[i32]]) -> Vec<Vec<i32>> {
    array.iter().map(|r| r.to_vec()).collect()
}

fn rotate_90_dyn(matrix: &Vec<Vec<i32>>) -> Vec<Vec<i32>> {
    if matrix.is_empty() {
        return vec![];
    }
    let r = matrix.len();
    let c = matrix[0].len();
    let mut out = vec![vec![0i32; r]; c];
    for i in 0..r {
        for j in 0..c {
            out[j][r - 1 - i] = matrix[i][j];
        }
    }
    out
}

fn transpose_dyn(matrix: &Vec<Vec<i32>>) -> Vec<Vec<i32>> {
    if matrix.is_empty() {
        return vec![];
    }
    let r = matrix.len();
    let c = matrix[0].len();
    let mut out = vec![vec![0i32; r]; c];
    for i in 0..r {
        for j in 0..c {
            out[j][i] = matrix[i][j];
        }
    }
    out
}

pub(crate) fn get_all_rotations_and_flips(array: &[&[i32]]) -> HashSet<Vec<Vec<i32>>> {
    let orig = to_owned_matrix(array);
    let mut results: HashSet<Vec<Vec<i32>>> = HashSet::new();

    // rotations of original
    let mut cur = orig.clone();
    for _ in 0..4 {
        results.insert(cur.clone());
        cur = rotate_90_dyn(&cur);
    }

    // rotations of flipped (transpose) version
    let flipped = transpose_dyn(&orig);
    cur = flipped.clone();
    for _ in 0..4 {
        results.insert(cur.clone());
        cur = rotate_90_dyn(&cur);
    }

    results
}
#[cfg(test)]
mod tests {
    use crate::arrays::rotate_90_dyn;

    #[test]
    fn test_rotate_90_dyn() {
        let matrix = vec![vec![1, 2, 3], vec![4, 5, 6], vec![7, 8, 9]];
        let rotated = rotate_90_dyn(&matrix);
        let expected = vec![vec![7, 4, 1], vec![8, 5, 2], vec![9, 6, 3]];
        assert_eq!(rotated, expected);
    }

    #[test]
    fn test_transpose_dyn() {
        let matrix = vec![vec![1, 2, 3], vec![4, 5, 6]];
        let transposed = super::transpose_dyn(&matrix);
        let expected = vec![vec![1, 4], vec![2, 5], vec![3, 6]];
        assert_eq!(transposed, expected);
    }
}
