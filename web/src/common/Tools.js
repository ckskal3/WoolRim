export const dataKey = (data, columnIndex) => {
  if(data.length === 0) return;
  const columns = Object.keys(data[0]);
  return columns[columnIndex];
}