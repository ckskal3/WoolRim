const util = require('util');
const exec = util.promisify(require('child_process').exec);
const fs = require('fs');
const path = require('path');


const background_paths = [
  path.join(__dirname, '../../../woolrim_storage/background_music/1.mp3'), // 잔잔한
  path.join(__dirname, '../../../woolrim_storage/background_music/2.mp3'), // 밝은
  path.join(__dirname, '../../../woolrim_storage/background_music/3.mp3'), // 어두운
];

const mixAudio = async (stu_id, file_name) => {
  const recording_path = path.join(__dirname + `../../../../woolrim_storage/${stu_id}/${file_name}`);
  const file_name_without_type = file_name.replace(/\..+$/, '').trim();
  if (!fs.existsSync(recording_path)) {
    console.log(`${recording_path} NOT FOUND`)
    return false;
  }
  let results = [];
  await Promise.all(background_paths.map(async (back, i) => {
    if (!fs.existsSync(back)) { return; }
    const result_path = path.join(__dirname, `../../../woolrim_storage/${stu_id}/${file_name_without_type}_${i + 1}.mp3`);
    results.push(result_path);
    if (fs.existsSync(result_path)) {
      fs.unlinkSync(result_path);
    }
    await exec(`ffmpeg -i ${recording_path} -i ${back} -filter_complex amerge ${result_path}`);
  }));
  return results.reduce((accumulator, currentValue) => {
    if (!fs.existsSync(currentValue)) { return accumulator && false; }
    return accumulator;
  }, true);
}

export default mixAudio