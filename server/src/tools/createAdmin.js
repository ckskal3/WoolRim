import { User } from '../model';
import pbkdf2 from 'pbkdf2';

const admin_list = [
  {
    name: '조수근',
    passwd: '1234',
  },
  {
    name: '조경환',
    passwd: '1234',
  },
  {
    name: '오동원',
    passwd: '1234',
  },
  {
    name: '홍지호',
    passwd: '1234',
  },
  {
    name: '박채은',
    passwd: '1234',
  },
]

const createAdmins = async () => {
  for (const i in admin_list) {
    const name = admin_list[i].name;
    const result = await User.where({ name, }).where({ admin: true }).one();
    if (!result) {
      console.log('관리자 '+name+' 생성');
      const encryption = await pbkdf2.pbkdf2Sync(admin_list[i].passwd, name, 30, 32, 'sha512');
      const user = new User({
        name,
        univ: 'admin',
        admin: true,
        stu_id: 0,
        gender: 'admin',
        passwd: encryption.toString(),
        created: new Date(),
        bongsa_time: 9999,
        profile: 'admin',
      });
      await user.save();
      console.log('생성 완료');
    }
  }
};
try {
  createAdmins();
} catch (e) {
  console.log(e);
}