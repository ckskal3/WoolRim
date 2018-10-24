export const dataKey = (data, columnIndex) => {
  if(data.length === 0) return;
  const columns = Object.keys(data[0]);
  return columns[columnIndex];
}

export const dateFormatter = (input_date) => {
  const date = new Date(input_date);
  return `${date.getFullYear()}-${date.getMonth()+1}-${date.getDate()}`
}