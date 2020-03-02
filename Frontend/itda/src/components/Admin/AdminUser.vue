<template>
    <v-card>
        <v-layout>
            <v-flex xs12="xs12">
                <v-card-title style="float:right">
                    <v-text-field
                        v-model="search"
                        append-icon="search"
                        label="Search"
                        single-line="single-line"
                        hide-details="hide-details"></v-text-field>
                </v-card-title>
            </v-flex>
        </v-layout>

        <v-data-table
            :headers="headers"
            :items-per-page="5"
            :items="allusers"
            :search="search">
            <template v-slot:item.major="{item}">
            <span v-if="item.major === null">X</span>
            <span v-else>{{item.major}}</span>
             
            </template>  
            <template v-slot:item.auth="{ item }">
            <span v-if="item.auth ===0 && item.uname ==='관리자'">관리자</span>
            <span v-else-if="item.auth === 0">매니저</span>
            <span v-else>유저</span>
            <v-icon v-if="item.uname !=='관리자'" small="small" class="mx-2" @click="ChangeItem(item)">
                    edit
            </v-icon>
            </template>  
            <template v-slot:item.action="{ item }">
                <v-icon small="small" @click="deleteItem(item)">
                    delete
                </v-icon>
            </template>
        </v-data-table>
    </v-card>
</template>

<script>
//관리자는 0
    import axios from 'axios'
    export default {
        name: "adminuser",
        methods: {
            getAllUsers() 
            {
                axios
                    .get('https://i02b201.p.ssafy.io:8197/itda/api/getAllUsers/')
                    .then(res => {
                        this.allusers =res.data
                    })
                    .catch(() => {})
            },
            deleteItem(item)
            {
                if( item.auth === 0)
                {
                    alert("권한이 관리자, 매니저일 경우 삭제할 수 없습니다.")
                }
                else 
                {
                    axios
                    .delete('https://i02b201.p.ssafy.io:8197/itda/api/deleteUser/'+ item.uid)
                    .then(res => {
                        if(res.data.state == 'success'){
                            this.getAllUsers();
                        }
                    })
                    .catch((err) => 
                    {
                        alert(err)
                    })

                }
            }
            ,
            ChangeItem(item)
            {
                alert(item.uid )
                if(item.uid === 4)
                {
                    alert("관리자는 권한 변경이 불가능 합니다.")
                }
                else
                {   
                    let uid = item.uid;
                    let auth =0;
                    if(item.auth === 0 )
                    {
                        auth = 1; 
                    }
                    else
                    {
                        auth = 0;
                    }

                    axios
                    .put('https://i02b201.p.ssafy.io:8197/itda/api/updatePermission/', { uid, auth })
                    .then(res => {
                        if(res.data.state == 'success'){
                            this.getAllUsers();
                        }
                    })
   
                }


            }
                
        },
        mounted()
        {
            this.getAllUsers()
        },
        data() {
            return {
                search:'',
                headers: [
                    {
                        text: 'UserImg',
                        align: 'center',
                        sortable: false,
                        value: 'uImg'
                    }
                    ,
                    {
                        text: 'UserName',
                        sortable: false,
                        value: 'uname'
                    }, {
                        text: 'UserEmail',
                        value: 'email'
                    },
                    {
                        text: 'Major',
                        align: 'center',
                        value: 'major',
                
                    },
                    {
                        text: '권한',
                        align: 'left',
                        value: 'auth',
                        sortable: false
                    } 
                    ,
                    {
                        text: '삭제',
                        align: 'center',
                        value: 'action',
                        sortable: false
                    }
                ],
                allusers: [
                    {
                      
                    }

                ]
            }
        }
    }
</script>

<style></style>