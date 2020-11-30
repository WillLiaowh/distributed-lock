package com.example.demo;

import org.junit.jupiter.api.Test;
//import org.redisson.RedissonRedLock;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class DistributedLockRedisApplicationTests {

    @Test
    void contextLoads() {
        int[] arr = {-567,78,0,23,-567,70, -1,900, -567};

        quickSort(arr, 0, arr.length - 1);

        System.out.println("Java3y   " + Arrays.toString(arr));
    }

    /**
     * 快速排序
     *
     * @param arr
     * @param L   指向数组第一个元素
     * @param R   指向数组最后一个元素
     */
    public static void quickSort(int[] arr, int L, int R) {
        int i = L;
        int j = R;

        //支点
        int pivot = arr[(L + R) / 2];

        //左右两端进行扫描，只要两端还没有交替，就一直扫描
        while (i <= j) {

            //寻找直到比支点大的数
            while (pivot > arr[i])
                i++;

            //寻找直到比支点小的数
            while (pivot < arr[j])
                j--;

            //此时已经分别找到了比支点小的数(右边)、比支点大的数(左边)，它们进行交换
            if (i <= j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        //上面一个while保证了第一趟排序支点的左边比支点小，支点的右边比支点大了。


        //“左边”再做排序，直到左边剩下一个数(递归出口)
        if (L < j)
            quickSort(arr, L, j);

        //“右边”再做排序，直到右边剩下一个数(递归出口)
        if (i < R)
            quickSort(arr, i, R);
    }



//    public void test(){
//        String resource = "resource";
//        String lock_id = "lock_id";
//        if(!checkReentrantLock(resource,lock_id)){
//            lock(resource,lock_id);//加锁
//        }else{
//            reentrantLock(resource,lock_id);    //可重入锁+1
//        }
//        //业务处理
//        unlock(resource,lock_id);//释放锁
//    }

//    @Autowired
//    RedissonClient redissonClient;
//    public void test(){
//       RLock rLock1 = redissonClient.getLock("lock1");
//       RLock rLock2 = redissonClient.getLock("lock2");
//       RLock rLock3 = redissonClient.getLock("lock3");
//       RedissonRedLock redLock = new RedissonRedLock(rLock1,rLock2,rLock3);
//       redLock.lock();
//        //业务处理
//       redLock.unlock();
//    }

}
