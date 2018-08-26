const getAllUser = () => {
  return [{
    id: 1,
    name: 'tnrms',
    stu_id: '201201548',
    gender: '남',
    passwd: '123445',
    created: '오늘',
    bongsa_time: 30,
  }]
}

const userResolver = {
  Query: {
    getAllUser,
  },
  Mutation: {

  }
}

export { userResolver };